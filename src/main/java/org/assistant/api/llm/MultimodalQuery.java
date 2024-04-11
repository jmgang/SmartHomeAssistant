package org.assistant.api.llm;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.PartMaker;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import org.assistant.api.util.FileUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class MultimodalQuery {

    public static void main(String[] args) throws Exception {
        String projectId = "smart-home-assistant-420017";
        String location = "us-central1";
        String modelName = "gemini-1.0-pro-vision";
            String filePath = "D:\\library\\Downloads\\434692882_837320391539903_3727005192433893916_n.png";

        String output = multimodalQuery(projectId, location, modelName, filePath);
        System.out.println(output);
    }


    // Ask the model to recognise the brand associated with the logo image.
    public static String multimodalQuery(String projectId, String location, String modelName,
                                         String filePath) throws Exception {
        try (VertexAI vertexAI = new VertexAI(projectId, location)) {
            byte[] imageBytes = Files.readAllBytes(Path.of(filePath));

            GenerativeModel model = new GenerativeModel(modelName, vertexAI);
            GenerateContentResponse response = model.generateContent(
                    ContentMaker.fromMultiModalData(
                            "You are a smart assistant. What do you see?",
                            PartMaker.fromMimeTypeAndData("image/png", imageBytes)
                    ));

            return ResponseHandler.getText(response);
        }
    }
}

