package org.assistant.api.news;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record News(Meta meta, List<Article> data) {

    public record Meta(int found, int returned, int limit, int page) {}

    public record Article(
            String title,
            String description,
            String snippet,
            String language,
            @JsonProperty("published_at") String publishedAt,
            String source,
            List<String> categories,
            String locale) {}

}
