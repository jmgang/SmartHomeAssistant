package org.assistant.assistant.tools;

import dev.langchain4j.agent.tool.Tool;

public class EnvironmentRecognizerService {

    @Tool("Captures an image of the environment and describes the image in the perspective of an agent.")
    public String recognizeEnvironmentByImage() {
        return "I see I'm inside a room.";
    }
}
