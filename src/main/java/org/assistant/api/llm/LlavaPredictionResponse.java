package org.assistant.api.llm;

import java.util.Map;

public record LlavaPredictionResponse(
        String id,
        String model,
        String version,
        Map<String, String> input,
        String logs,
        String error,
        String status,
        String createdAt,
        Urls urls
) {
    public record Urls(
            String cancel,
            String get
    ) {}
}
