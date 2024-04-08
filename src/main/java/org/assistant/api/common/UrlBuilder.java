package org.assistant.api.common;

import okhttp3.HttpUrl;

import java.util.Map;
import java.util.Objects;

public class UrlBuilder {
    private final HttpUrl.Builder urlBuilder;

    public UrlBuilder(String baseUrl) {
        this.urlBuilder = Objects.requireNonNull(HttpUrl.parse(baseUrl)).newBuilder();
    }

    public UrlBuilder addPathSegments(String... segments) {
        for (String segment : segments) {
            this.urlBuilder.addPathSegment(segment);
        }
        return this;
    }

    public UrlBuilder addQueryParams(Map<String, String> queryParams) {
        if (queryParams != null) {
            queryParams.forEach(this.urlBuilder::addQueryParameter);
        }
        return this;
    }

    public String build() {
        return this.urlBuilder.build().toString();
    }
}
