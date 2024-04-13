package org.assistant.assistant.core;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.assistant.assistant.constants.Prompts;
import org.assistant.assistant.core.assistants.Intent;
import org.assistant.assistant.core.assistants.IntentControllerAssistant;
import org.assistant.assistant.core.assistants.service.ActionAIAssistantService;
import org.assistant.assistant.core.assistants.service.ConversationalAIAssistantService;

import java.util.Arrays;
import java.util.List;

import static dev.langchain4j.data.message.SystemMessage.systemMessage;
import static java.time.Duration.ofSeconds;
import static org.assistant.config.ConfigLoader.getProperty;

public class SmartHomeAssistantManager {

    private final ChatLanguageModel chatLanguageModel;

    private final ConversationalAIAssistantService conversationalAIAssistantService;

    private final ActionAIAssistantService actionAIAssistantService;

    private final IntentControllerAssistant intentControllerAssistant;

    public SmartHomeAssistantManager() {
        chatLanguageModel = OpenAiChatModel.builder()
                .apiKey(getProperty("assistant.openai.apikey"))
                .timeout(ofSeconds(200))
                //.logRequests(true)
                //.logResponses(true)
                .build();

        var chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        chatMemory.add(systemMessage(Prompts.SMART_HOME_ASST_SYS_MESSAGE));

        conversationalAIAssistantService = new ConversationalAIAssistantService(chatLanguageModel, chatMemory);
        actionAIAssistantService = new ActionAIAssistantService(chatLanguageModel, chatMemory);
        intentControllerAssistant = AiServices.create(IntentControllerAssistant.class, chatLanguageModel);
    }

    public ChatLanguageModel model() {
        return chatLanguageModel;
    }

    public String query(String query) {
        Intent intent = intentControllerAssistant.specifyIntent(query);

        System.out.println("Intent: " + intent);

        if(intent == null || intent.isConversational()) {
            return conversationalAIAssistantService.chat(query);
        }

        return actionAIAssistantService.sendActionCommand(query);
    }


    public static void main(String[] args) {
        SmartHomeAssistantManager smartHomeAssistantManager = new SmartHomeAssistantManager();

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
                    smartHomeAssistantManager.query(s) +
                    //assistant.chat(s) +
                    "\n");
        }
    }

}
