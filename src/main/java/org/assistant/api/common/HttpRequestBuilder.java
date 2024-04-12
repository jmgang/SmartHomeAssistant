package org.assistant.api.common;

import okhttp3.HttpUrl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HttpRequestBuilder {
    private final HttpUrl.Builder urlBuilder;

    private final Map<String, String> headers = new HashMap<>();

    public HttpRequestBuilder(String baseUrl) {
        this.urlBuilder = Objects.requireNonNull(HttpUrl.parse(baseUrl)).newBuilder();
    }

    public HttpRequestBuilder addPathSegments(String... segments) {
        for (String segment : segments) {
            this.urlBuilder.addPathSegment(segment);
        }
        return this;
    }

    public HttpRequestBuilder addQueryParams(Map<String, String> queryParams) {
        if (queryParams != null) {
            queryParams.forEach(this.urlBuilder::addQueryParameter);
        }
        return this;
    }

    public HttpRequestBuilder addHeader(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    public HttpRequestBuilder addHeaders(Map<String, String> headers) {
        if (headers != null) {
            this.headers.putAll(headers);
        }
        return this;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }


    public String build() {
        return this.urlBuilder.build().toString();
    }
}
