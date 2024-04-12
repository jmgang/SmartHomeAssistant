package org.assistant.api.llm;

import org.assistant.api.common.HttpRequestBuilder;
import org.assistant.api.common.SimpleHttpClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Map;

import static org.assistant.config.ConfigLoader.getProperty;

public class LlavaClient {

    private final SimpleHttpClient httpClient = new SimpleHttpClient();

    public LlavaPredictionResponse generate() throws IOException {
        HttpRequestBuilder httpRequestBuilder = new HttpRequestBuilder(getProperty("assistant.api.replicate.url"))
                .addPathSegments("v1", "predictions")
                .addHeader("Authorization", "Bearer " + getProperty("assistant.api.replicate.apikey"))
                .addHeader("Content-Type", "application/json");

        String url = httpRequestBuilder.build();
        Map<String, String> headers = httpRequestBuilder.getHeaders();

        Path imagePath = Path.of("D:\\library\\Pictures\\364230023_706728411263893_5031803736007882232_n.jpg");
        String base64Image;
        try {
            base64Image = "data:image/jpeg;base64," + encodeFileToBase64(imagePath);
        } catch (IOException e) {
            System.err.println("Error encoding file to Base64: " + e.getMessage());
            throw new IOException("Error encoding file to Base64: " + e.getMessage());
        }

        LlavaPredictionRequest request = new LlavaPredictionRequest(
                "452b2fa0b66d8acdf40e05a7f0af948f9c6065f6da5af22fce4cead99a26ff3d",
                Map.of("image", base64Image)
        );

        try {
            LlavaPredictionResponse response = httpClient.sendPost(url, headers, request, LlavaPredictionResponse.class);
            System.out.println("Response: " + response);

            return response;
        } catch (IOException e) {
            System.err.println("An error occurred while making the POST request: " + e.getMessage());
            e.printStackTrace();

            throw new IOException("An error occurred while making the POST request: " + e.getMessage());
        }
    }

    private static String encodeFileToBase64(Path filePath) throws IOException {
        byte[] fileContent = Files.readAllBytes(filePath);
        return Base64.getEncoder().encodeToString(fileContent);
    }

    public static void main(String[] args) {
        LlavaClient llavaClient = new LlavaClient();

        try {
            llavaClient.generate();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
