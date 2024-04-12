package org.assistant.api.news;

import org.assistant.api.common.SimpleHttpClient;
import org.assistant.api.common.HttpRequestBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static org.assistant.config.ConfigLoader.getProperty;

public class NewsRetriever {

    private final SimpleHttpClient simpleHttpClient = new SimpleHttpClient();

    public Optional<News> retrieveTopStories() {
        HttpRequestBuilder httpRequestBuilder = new HttpRequestBuilder(getProperty("assistant.api.news.url"))
                .addPathSegments("top")
                .addQueryParams(Map.of("api_token", getProperty("assistant.api.news.apikey"),
                                    "limit", getProperty("assistant.api.news.limit"),
                                    "locale", getProperty("assistant.api.news.locale"),
                                    "language", getProperty("assistant.api.news.language")));

        try {
            return Optional.of(simpleHttpClient.sendGet(httpRequestBuilder.build(), News.class));
        }catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
