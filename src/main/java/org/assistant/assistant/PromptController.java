package org.assistant.assistant;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.assistant.assistant.rag.DocumentRetriever;
import org.assistant.assistant.tools.CurrentInformationRetrieverService;
import org.assistant.assistant.tools.EnvironmentRecognizerService;
import org.assistant.assistant.tools.SmartOutletManagerService;

import java.util.Arrays;
import java.util.List;

import static java.time.Duration.ofSeconds;

public class PromptController {

    interface Assistant {
        String chat(String message);
    }

    public static void main(String[] args) {

        var model = OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .timeout(ofSeconds(200))
                .logRequests(true)
                .build();

        var assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(model)
                .tools(new CurrentInformationRetrieverService(),
                        new EnvironmentRecognizerService(),
                        new SmartOutletManagerService())
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .contentRetriever(DocumentRetriever.get())
                .build();

        List<String> queries = Arrays.asList(
                "Can you turn on the lights please?",
                "What's the weather today?",
                "Give me the latest news",
                "Why is the sky blue?",
                "What can you see?",
                "What's today again?",
                "What are EAC's linkages?"
        );

        for(String s: queries) {
            System.out.println("[USER]: " + s + "\nLLM: " + assistant.chat(s) + "\n");
        }
    }

}
