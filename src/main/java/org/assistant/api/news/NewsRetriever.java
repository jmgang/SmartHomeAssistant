package org.assistant.api.news;

import org.assistant.api.SimpleHttpClient;
import org.assistant.api.common.UrlBuilder;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static org.assistant.config.ConfigLoader.getProperty;

public class NewsRetriever {

    private final SimpleHttpClient simpleHttpClient = new SimpleHttpClient();

    // https://api.thenewsapi.com/v1/news/top?api_token=YOUR_API_TOKEN&locale=us&limit=3

    public Optional<News> retrieveTopStories() {
        UrlBuilder urlBuilder = new UrlBuilder(getProperty("assistant.api.news.url"))
                .addPathSegments("top")
                .addQueryParams(Map.of("api_token", getProperty("assistant.api.news.apikey"),
                                    "limit", getProperty("assistant.api.news.limit"),
                                    "locale", getProperty("assistant.api.news.locale"),
                                    "language", getProperty("assistant.api.news.language")));

        try {
            return Optional.of(simpleHttpClient.sendGet(urlBuilder.build(), News.class));
        }catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
