package org.assistant.assistant.core.assistants.service;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import org.assistant.assistant.core.assistants.ActionAIAssistant;
import org.assistant.assistant.tools.CurrentInformationRetrieverService;
import org.assistant.assistant.tools.EnvironmentRecognizerService;
import org.assistant.assistant.tools.SmartOutletManagerService;

public class ActionAIAssistantService {

    private final ActionAIAssistant actionAIAssistant;
    public ActionAIAssistantService(ChatLanguageModel chatLanguageModel, ChatMemory chatMemory) {
        actionAIAssistant = AiServices.builder(ActionAIAssistant.class)
                .chatLanguageModel(chatLanguageModel)
                .chatMemory(chatMemory)
                .tools(new CurrentInformationRetrieverService(),
                        new EnvironmentRecognizerService(),
                        new SmartOutletManagerService())
                .build();
    }

    public String sendActionCommand(String command) {
        return actionAIAssistant.sendActionCommand(command);
    }

}


