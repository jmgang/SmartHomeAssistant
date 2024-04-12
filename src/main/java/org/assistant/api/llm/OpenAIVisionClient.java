package org.assistant.api.llm;

import org.assistant.api.common.HttpRequestBuilder;
import org.assistant.api.common.SimpleHttpClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;

import static org.assistant.config.ConfigLoader.getProperty;

public class OpenAIVisionClient {
    private final SimpleHttpClient httpClient = new SimpleHttpClient();

    public OpenAIResponse analyzeImage(Path imagePath) throws IOException {
        String base64Image = encodeFileToBase64(imagePath);
        var imageUrl = new OpenAIRequest.Content.ImageUrl("data:image/jpeg;base64," + base64Image);
        var contentList = List.of(
                new OpenAIRequest.Content("text", "What’s in this image?", null),
                new OpenAIRequest.Content("image_url", null, imageUrl));
        var message = new OpenAIRequest.Message("user", contentList);
        var request = new OpenAIRequest("gpt-4-turbo", List.of(message), 300);

        HttpRequestBuilder httpRequestBuilder = new HttpRequestBuilder("https://api.openai.com/v1/chat/completions")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + getProperty("assistant.openai.apikey"));

        return httpClient.sendPost(httpRequestBuilder.build(), httpRequestBuilder.getHeaders(), request, OpenAIResponse.class);
    }

    private static String encodeFileToBase64(Path filePath) throws IOException {
        byte[] fileContent = Files.readAllBytes(filePath);
        return Base64.getEncoder().encodeToString(fileContent);
    }

    public static void main(String[] args) {
        OpenAIVisionClient openAIClient = new OpenAIVisionClient();

        try {
            openAIClient.analyzeImage(Path.of("D:\\library\\Downloads\\433999893_941048247604337_895598583215439361_n.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
