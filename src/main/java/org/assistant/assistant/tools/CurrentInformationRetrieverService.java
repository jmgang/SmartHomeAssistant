package org.assistant.assistant.tools;

import dev.langchain4j.agent.tool.Tool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.time.LocalDateTime;

public class CurrentInformationRetrieverService {

    @Tool("Retrieves the latest news.")
    public String retrieveNews() {

        // amdOUNB1YcVVQcHz8HMi07YcprB3hnwgoBfGPycM

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://example.com/api/resource") // Replace with your API URL
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // For a string response body. For other types, you may use response.body().bytes() or .byteStream()
            System.out.println(response.body().string());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Latest News: President proclaims April 10th is a holiday.";
    }
    @Tool("Retrieves the latest weather.")
    public String retrieveWeather() {
        return "Weather today is 34°C°F\n" +
                "Precipitation: 10%\n" +
                "Humidity: 67%\n" +
                "Wind: 11 km/h";
    }

    @Tool("Retrieves the date and time.")
    public String retrieveTime() {
        return LocalDateTime.now().toString();
    }
}
