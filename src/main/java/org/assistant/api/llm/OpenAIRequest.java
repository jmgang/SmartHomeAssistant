package org.assistant.api.llm;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OpenAIRequest(String model, List<Message> messages, int maxTokens) {
    public record Message(String role, List<Content> content) {}

    public record Content(String type, String text, @JsonProperty("image_url") ImageUrl imageUrl) {
        public record ImageUrl(String url) {}
    }
}

