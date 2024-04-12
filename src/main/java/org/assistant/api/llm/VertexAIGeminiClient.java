package org.assistant.api.llm;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.PartMaker;
import com.google.cloud.vertexai.generativeai.ResponseHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assistant.config.ConfigLoader.getProperty;

public class VertexAIGeminiClient {

    public static String analyzeImage(String filePath) throws Exception {
        try (VertexAI vertexAI = new VertexAI(getProperty("assistant.api.googlecloud.projectid"),
                getProperty("assistant.api.googlecloud.location"))) {
            byte[] imageBytes = Files.readAllBytes(Path.of(filePath));

            GenerativeModel model = new GenerativeModel(getProperty("assistant.api.googlecloud.vertexai.modelname"), vertexAI);
            GenerateContentResponse response = model.generateContent(
                    ContentMaker.fromMultiModalData(
                            "You are a smart assistant. What do you see?",
                            PartMaker.fromMimeTypeAndData("image/png", imageBytes)
                    ));

            return ResponseHandler.getText(response);
        }
    }

    public static void multimodalVideoInput(String projectId, String location, String modelName)
            throws IOException {
        // Initialize client that will be used to send requests. This client only needs
        // to be created once, and can be reused for multiple requests.
        try (VertexAI vertexAI = new VertexAI(projectId, location)) {
            String videoUri = "gs://sample_bucket_smartassistant/sample sign language 2.mp4";

            GenerativeModel model = new GenerativeModel(modelName, vertexAI);
            GenerateContentResponse response = model.generateContent(
                    ContentMaker.fromMultiModalData(
                            "You are a sign language expert. A person in this video is doing sign language. " +
                                    "Tell me what the person says.",
                            PartMaker.fromMimeTypeAndData("video/mp4", videoUri)
                    ));

            String output = ResponseHandler.getText(response);
            System.out.println("OUTPUT");
            System.out.println(output);
        }
    }

    public static void main(String[] args) {
        try {
            multimodalVideoInput(getProperty("assistant.api.googlecloud.projectid"),
                    "us-central1", "gemini-1.5-pro-preview-0409");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

