package org.assistant.api.weather;

import org.assistant.api.common.SimpleHttpClient;
import org.assistant.api.common.HttpRequestBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static org.assistant.config.ConfigLoader.getProperty;

public class WeatherRetriever {

    private final SimpleHttpClient simpleHttpClient = new SimpleHttpClient();

    public Optional<Weather> retrieveCurrentWeather() {
        HttpRequestBuilder httpRequestBuilder = new HttpRequestBuilder(getProperty("assistant.api.weather.url"))
                .addPathSegments("v1", "current.json")
                .addQueryParams(Map.of("key", getProperty("assistant.api.weather.apikey"),
                        "q", getProperty("assistant.api.weather.query")));

        try {
            return Optional.of(simpleHttpClient.sendGet(httpRequestBuilder.build(), Weather.class));
        }catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
