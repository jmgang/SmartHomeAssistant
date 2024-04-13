package org.assistant.assistant.core.assistants;

import dev.langchain4j.service.UserMessage;

public interface IntentControllerAssistant {

    @UserMessage("Your role is to specify the intent of a given query for a smart home assistant with camera: {{it}}. " +
            "An intent can only be NOT_SUPPORTED or ACTION. Return only either of those and a confidence score." +
            "An action can ONLY be one or related to one of the following. Anything that does not appear from below is NOT an action" +
            "1. Ability to see" +
            "2. Retrieving the news, weather or date/time" +
            "3. Controlling external smart devices such as an outlet or bulb.")
    String specifyIntent(String text);
}
