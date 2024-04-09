package org.assistant.api.common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class SimpleHttpClient {

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SimpleHttpClient() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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
}
