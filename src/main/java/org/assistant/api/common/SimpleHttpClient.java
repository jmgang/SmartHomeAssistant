package org.assistant.api.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SimpleHttpClient {

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SimpleHttpClient() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public <T> T sendGet(String url,  Class<T> responseType) throws IOException {
        return sendGet(url, null, responseType);
    }

    public <T> T sendGet(String url, Map<String, String> headers, Class<T> responseType) throws IOException {
        Request.Builder builder = new Request.Builder().url(url);

        if (headers != null) {
            headers.forEach(builder::addHeader);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String responseBody = Objects.requireNonNull(response.body()).string();
            return objectMapper.readValue(responseBody, responseType); // Deserialize JSON string to Java object
        }
    }

    public <T, R> R sendPost(String url, Map<String, String> headers, T requestBodyObject, Class<R> responseType) throws IOException {
        // Serialize the Java object to JSON string
        String jsonBody = objectMapper.writeValueAsString(requestBodyObject);

        System.out.println(jsonBody);

        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));

        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(body);

        if (headers != null) {
            headers.forEach(builder::addHeader);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String responseBody = Objects.requireNonNull(response.body()).string();
            // Deserialize JSON response to Java object
            return objectMapper.readValue(responseBody, responseType);
        }
    }

    public <T> T sendPost(String url, Map<String, String> headers, Object requestBodyObject, TypeReference<T> typeReference) throws IOException {
        String jsonBody = objectMapper.writeValueAsString(requestBodyObject);
        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));
        Request.Builder builder = new Request.Builder().url(url).post(body);
        if (headers != null) {
            headers.forEach(builder::addHeader);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String responseBody = Objects.requireNonNull(response.body()).string();

            // Log the response body
            System.out.println("Response body:");
            System.out.println(responseBody);

            return objectMapper.readValue(responseBody, typeReference);
        }
    }


    private String serializeToJson(Map<String, Object> data) throws JsonProcessingException {
        return objectMapper.writeValueAsString(data);
    }
}
