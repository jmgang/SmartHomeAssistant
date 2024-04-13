package org.assistant.assistant.core.assistants.service;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import org.assistant.assistant.core.assistants.ConversationalAIAssistant;
import org.assistant.assistant.rag.DocumentRetriever;
public class ConversationalAIAssistantService {

    private final ConversationalAIAssistant conversationalAIAssistant;
    public ConversationalAIAssistantService(ChatLanguageModel chatLanguageModel, ChatMemory chatMemory) {
        conversationalAIAssistant = AiServices.builder(ConversationalAIAssistant.class)
                .chatLanguageModel(chatLanguageModel)
                .chatMemory(chatMemory)
                .contentRetriever(DocumentRetriever.get())
                .build();
    }

    public String chat(String message) {
        return conversationalAIAssistant.chat(message);
    }

}
