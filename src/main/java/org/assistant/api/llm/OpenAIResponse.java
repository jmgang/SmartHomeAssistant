package org.assistant.api.llm;

import java.util.List;

public record OpenAIResponse(String id, String object, List<Completion> completions) {
    public record Completion(String model, String created, int maxTokens, List<Choice> choices) {
        public record Choice(String text, String index, String logprobs, int finishReason) {}
    }
}
