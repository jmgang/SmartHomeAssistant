package org.assistant.api.llm;

import java.util.Map;

public record LlavaPredictionRequest(String version, Map<String, String> input) {}