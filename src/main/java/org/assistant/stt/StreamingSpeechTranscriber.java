// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: Apache-2.0

package org.assistant.stt;

import org.assistant.assistant.ConversationalAIAssistant;
import org.assistant.tts.PollySpeaker;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.transcribestreaming.TranscribeStreamingAsyncClient;
import software.amazon.awssdk.services.transcribestreaming.model.*;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class StreamingSpeechTranscriber {
    private static final Region REGION = Region.US_EAST_1;
    private static TranscribeStreamingAsyncClient client;

    private static ConversationalAIAssistant assistant;

    // private final TranscriptionResultHandler resultHandler;

//    public StreamingSpeechTranscriber(TranscriptionResultHandler resultHandler) {
//        this.resultHandler = resultHandler;
//        this.client = TranscribeStreamingAsyncClient.builder()
//                .credentialsProvider(getCredentials())
//                .region(REGION)
//                .build();
//    }

//    public void startTranscription() throws LineUnavailableException {
//        CompletableFuture<Void> result = client.startStreamTranscription(
//                getRequest(16_000),
//                new AudioStreamPublisher(getStreamFromMic()),
//                getResponseHandler());
//
//        result.whenComplete((r, e) -> {
//            if (e != null) {
//                resultHandler.onError((Exception) e);
//            }
//            client.close();
//        });
//    }

    public static void main(String args[])
            throws URISyntaxException, ExecutionException, InterruptedException, LineUnavailableException {

        client = TranscribeStreamingAsyncClient.builder()
                .credentialsProvider(getCredentials())
                .region(REGION)
                .build();

        assistant = new ConversationalAIAssistant();

        CompletableFuture<Void> result = client.startStreamTranscription(getRequest(16_000),
                new AudioStreamPublisher(getStreamFromMic()),
                getResponseHandler());

        result.get();
        client.close();
    }

    private static InputStream getStreamFromMic() throws LineUnavailableException {

        // Signed PCM AudioFormat with 16kHz, 16 bit sample size, mono
        int sampleRate = 16000;
        AudioFormat format = new AudioFormat(sampleRate, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Line not supported");
            System.exit(0);
        }

        TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
        line.open(format);
        line.start();

        InputStream audioStream = new AudioInputStream(line);
        return audioStream;
    }

    private static AwsCredentialsProvider getCredentials() {
        return ProfileCredentialsProvider.create("admin-general");
    }

    private static StartStreamTranscriptionRequest getRequest(Integer mediaSampleRateHertz) {
        return StartStreamTranscriptionRequest.builder()
                .languageCode(LanguageCode.EN_US.toString())
                .mediaEncoding(MediaEncoding.PCM)
                .mediaSampleRateHertz(mediaSampleRateHertz)
                .build();
    }

     private static StartStreamTranscriptionResponseHandler getResponseHandler() {
        return StartStreamTranscriptionResponseHandler.builder()
                .onResponse(r -> System.out.println("Received Initial response"))
                .onError(e -> {
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    System.out.println("Error occurred.");
                })
                .onComplete(() -> System.out.println("=== All records stream successfully ==="))
                .subscriber(event -> {
                    List<Result> results = ((TranscriptEvent) event).transcript().results();
                    if (!results.isEmpty()) {
                        Result result = results.get(0);
                        if (!result.isPartial() && !result.alternatives().isEmpty() && !result.alternatives().get(0).transcript().isEmpty()) {
                            String transcript = result.alternatives().get(0).transcript();
                            System.out.println("Transcript: " + transcript);

                            String chatResponse = assistant.chat(transcript);

                            System.out.println("RESPONSE: " + chatResponse);

                            PollySpeaker.talk(chatResponse);
                        }
                    }
                })
                .build();
    }

    private InputStream getStreamFromFile(String audioFileName) {
        try {
            File inputFile = new File(getClass().getClassLoader().getResource(audioFileName).getFile());
            InputStream audioStream = new FileInputStream(inputFile);
            return audioStream;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static class AudioStreamPublisher implements Publisher<AudioStream> {
        private final InputStream inputStream;
        private static Subscription currentSubscription;

        private AudioStreamPublisher(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public void subscribe(Subscriber<? super AudioStream> s) {

            if (this.currentSubscription == null) {
                this.currentSubscription = new SubscriptionImpl(s, inputStream);
            } else {
                this.currentSubscription.cancel();
                this.currentSubscription = new SubscriptionImpl(s, inputStream);
            }
            s.onSubscribe(currentSubscription);
        }
    }

    public static class SubscriptionImpl implements Subscription {
        private static final int CHUNK_SIZE_IN_BYTES = 1024 * 1;
        private final Subscriber<? super AudioStream> subscriber;
        private final InputStream inputStream;
        private ExecutorService executor = Executors.newFixedThreadPool(1);
        private AtomicLong demand = new AtomicLong(0);

        SubscriptionImpl(Subscriber<? super AudioStream> s, InputStream inputStream) {
            this.subscriber = s;
            this.inputStream = inputStream;
        }

        @Override
        public void request(long n) {
            if (n <= 0) {
                subscriber.onError(new IllegalArgumentException("Demand must be positive"));
            }

            demand.getAndAdd(n);

            if (!executor.isShutdown()) {
                executor.submit(() -> {
                    try {
                        do {
                            ByteBuffer audioBuffer = getNextEvent();
                            if (audioBuffer.remaining() > 0) {
                                AudioEvent audioEvent = audioEventFromBuffer(audioBuffer);
                                subscriber.onNext(audioEvent);
                            } else {
                                subscriber.onComplete();
                                break;
                            }
                        } while (demand.decrementAndGet() > 0);
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                });
            } else {
                //System.out.println("Executor shutdown...");
            }

        }

        @Override
        public void cancel() {
            executor.shutdown();
        }

        private ByteBuffer getNextEvent() {
            ByteBuffer audioBuffer = null;
            byte[] audioBytes = new byte[CHUNK_SIZE_IN_BYTES];

            int len = 0;
            try {
                len = inputStream.read(audioBytes);

                if (len <= 0) {
                    audioBuffer = ByteBuffer.allocate(0);
                } else {
                    audioBuffer = ByteBuffer.wrap(audioBytes, 0, len);
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }

            return audioBuffer;
        }

        private AudioEvent audioEventFromBuffer(ByteBuffer bb) {
            return AudioEvent.builder()
                    .audioChunk(SdkBytes.fromByteBuffer(bb))
                    .build();
        }
    }
}
// snippet-end:[transcribe.java-streaming-demo]
