package org.assistant.assistant.core.assistants;

import dev.langchain4j.service.UserMessage;

public interface IntentControllerAssistant {

    @UserMessage("Your role is to specify the intent of a given query for a smart home assistant with camera: {{it}}. " +
            "Your only response should only be the intents: " +
            "CONVERSATIONAL_INTENT, TOGGLE_OUTLET_INTENT, INQUIRE_WEATHER_INTENT, INQUIRE_DATETIME_INTENT, INQUIRE_NEWS_INTENT, SEE_INTENT " +
            "EXAMPLES: " +
            "Why is the sky blue? " +
            "CONVERSATIONAL_INTENT" +
            "Explanation: This is a general question asking about the sky and not about a still image that was captured." +
            "" +
            "Where are you located right now?" +
            "SEE_INTENT" +
            "Explanation: The intent was asking your location and thus where you are right now.")
    Intent specifyIntent(String text);
}
