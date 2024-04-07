package org.assistant.stt;

public interface TranscriptionResultHandler {
    void onTranscription(String transcript);
    void onError(Exception e);
}
