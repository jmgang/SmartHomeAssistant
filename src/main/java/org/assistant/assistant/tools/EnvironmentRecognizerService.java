package org.assistant.assistant.tools;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.output.Response;

import java.time.Duration;

public class EnvironmentRecognizerService {

    @Tool("Captures an image of the environment and describes the image in the perspective of an agent.")
    public String recognizeEnvironmentByImage() {


        return "I see I'm inside a room.";
    }

}
