package org.assistant.assistant.core;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.assistant.assistant.constants.Prompts;
import org.assistant.assistant.core.assistants.IntentControllerAssistant;
import org.assistant.assistant.core.assistants.SmartHomeAssistant;
import org.assistant.assistant.tools.CurrentInformationRetrieverService;
import org.assistant.assistant.tools.EnvironmentRecognizerService;
import org.assistant.assistant.tools.SmartOutletManagerService;

import java.util.Arrays;
import java.util.List;

import static dev.langchain4j.data.message.SystemMessage.systemMessage;
import static java.time.Duration.ofSeconds;
import static org.assistant.config.ConfigLoader.getProperty;

public class SmartHomeAssistantManager {

    private final ChatLanguageModel chatLanguageModel;

    private final SmartHomeAssistant smartHomeAssistant;

    public SmartHomeAssistantManager() {
        chatLanguageModel = OpenAiChatModel.builder()
                .apiKey(getProperty("assistant.openai.apikey"))
                .timeout(ofSeconds(200))
                //.logRequests(true)
                .logResponses(true)
                .build();

        var chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        chatMemory.add(systemMessage(Prompts.SMART_HOME_ASST_SYS_MESSAGE));

        smartHomeAssistant = AiServices.builder(SmartHomeAssistant.class)
                .chatLanguageModel(chatLanguageModel)
                .tools(new CurrentInformationRetrieverService(),
                        new EnvironmentRecognizerService(),
                        new SmartOutletManagerService())
                .chatMemory(chatMemory)
                //.contentRetriever(DocumentRetriever.get())
                .build();

    }

    public ChatLanguageModel model() {
        return chatLanguageModel;
    }

    public String query(String query) {
        return smartHomeAssistant.chat(query);
    }


    public static void main(String[] args) {
        SmartHomeAssistantManager smartHomeAssistantManager = new SmartHomeAssistantManager();
        var intentAssistant = AiServices.create(IntentControllerAssistant.class, smartHomeAssistantManager.model());

        List<String> queries = Arrays.asList(
                "Can you turn on the strip please?"
                , "Turn on the last outlet."
                , "Just turn it off"
                , "What's the weather today?"
                , "Why does it feel hot today?"
                , "Give me a brief view of the news today"
                , "Why is the sky blue?"
                 , "What can you see?"
                , "What's today again?"
                , "Describe the room you are in"
                , "What are Emilio Aguinaldo College's linkages?"
        );


        for(String s: queries) {
            System.out.println("[USER]: " + s + "\n[LLM]: " +
                    intentAssistant.specifyIntent(s) +
                    //assistant.chat(s) +
                    "\n");
        }
    }

}
