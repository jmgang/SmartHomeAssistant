package org.assistant.assistant.tools;

import dev.langchain4j.agent.tool.Tool;
import org.assistant.api.llm.VertexAIGeminiClient;

public class EnvironmentRecognizerService {

    @Tool("Captures an image of the environment and describes the image in the perspective of an agent.")
    public String recognizeEnvironmentByImage() {
        try {
            return VertexAIGeminiClient.analyzeImage("");
        } catch (Exception e) {
            return "Having trouble processing an image.";
        }
    }

}
