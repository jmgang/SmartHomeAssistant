package org.assistant.assistant.core;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiModerationModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.ModerationException;
import org.assistant.assistant.constants.Prompts;
import org.assistant.assistant.core.assistants.Intent;
import org.assistant.assistant.core.assistants.IntentControllerAssistant;
import org.assistant.assistant.core.assistants.Moderator;
import org.assistant.assistant.core.assistants.service.ActionAIAssistantService;
import org.assistant.assistant.core.assistants.service.ConversationalAIAssistantService;
import org.assistant.tts.PollySpeaker;

import java.util.*;

import static dev.langchain4j.data.message.SystemMessage.systemMessage;
import static java.time.Duration.ofSeconds;
import static org.assistant.config.ConfigLoader.getProperty;

public class SmartHomeAssistantManager {

    private final ChatLanguageModel chatLanguageModel;

    private final OpenAiModerationModel moderationModel;

    private final ConversationalAIAssistantService conversationalAIAssistantService;

    private final ActionAIAssistantService actionAIAssistantService;

    private final IntentControllerAssistant intentControllerAssistant;

    private final Moderator moderator;

    public SmartHomeAssistantManager() {
        chatLanguageModel = OpenAiChatModel.builder()
                .apiKey(getProperty("assistant.openai.apikey"))
                .timeout(ofSeconds(200))
                //.logRequests(true)
                //.logResponses(true)
                .build();

        moderationModel = OpenAiModerationModel.withApiKey(getProperty("assistant.openai.apikey"));
        moderator = AiServices.builder(Moderator.class)
                    .chatLanguageModel(chatLanguageModel)
                    .moderationModel(moderationModel).build();

        intentControllerAssistant = AiServices.builder(IntentControllerAssistant.class)
                                        .chatLanguageModel(chatLanguageModel)
                                        .build();

        var chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        chatMemory.add(systemMessage(Prompts.SMART_HOME_ASST_SYS_MESSAGE));

        conversationalAIAssistantService = new ConversationalAIAssistantService(chatLanguageModel, chatMemory);
        actionAIAssistantService = new ActionAIAssistantService(chatLanguageModel, chatMemory);

    }

    public ChatLanguageModel model() {
        return chatLanguageModel;
    }

    public String query(String query) {

        try {
            moderator.moderate(query);
        }catch (ModerationException me) {
            return "Please avoid making statements that violate the content policy.";
        }

        Intent intent = intentControllerAssistant.specifyIntent(query);

        System.out.println("Intent: " + intent);

        String response;
        if(intent == null || intent.isConversational()) {
            response = conversationalAIAssistantService.chat(query);
        }else {
            response = actionAIAssistantService.sendActionCommand(query);
        }

        try {
            moderator.moderate(response);
        }catch (ModerationException me) {
            return "We restricted the response made by one of our LLMs that maybe harmful to you. We take ethical responses very seriously.";
        }

        return response;
    }


}
