package org.assistant.assistant;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.structured.Description;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import org.assistant.assistant.constants.Prompts;
import org.assistant.assistant.rag.DocumentRetriever;
import org.assistant.assistant.tools.CurrentInformationRetrieverService;
import org.assistant.assistant.tools.EnvironmentRecognizerService;
import org.assistant.assistant.tools.SmartOutletManagerService;

import java.util.Arrays;
import java.util.List;

import static dev.langchain4j.data.message.SystemMessage.systemMessage;
import static java.time.Duration.ofSeconds;
import static org.assistant.config.ConfigLoader.getProperty;

public class PromptController {

    public enum INTENT {

        @Description("An intent of solely conversing or asking questions.")
        CONVERSATIONAL,

        @Description("An intent of solely performing an action or command. An action can only be one of the following: " +
                "1. Capturing an image of the environment" +
                "2. Retrieving current information such news, weather or date/time." +
                "3. Controlling external smart devices such as an outlet or bulb.")
        ACTION
    }

    interface Assistant {
        String chat(String message);
    }

    interface IntentControllerAssistant {

        @UserMessage("Your role is to specify the intent of a given query for a smart home assistant with camera: {{it}}. " +
                "An intent can only be NOT_SUPPORTED or ACTION. Return only either of those and a confidence score." +
                "An action can ONLY be one or related to one of the following. Anything that does not appear from below is NOT an action" +
                "1. Ability to see" +
                "2. Retrieving the news, weather or date/time" +
                "3. Controlling external smart devices such as an outlet or bulb.")
        String specifyIntent(String text);
    }

    public static void main(String[] args) {

        var model = OpenAiChatModel.builder()
                .apiKey(getProperty("assistant.openai.apikey"))
                .timeout(ofSeconds(200))
                .logRequests(true)
                //.logResponses(true)
                .build();

        var chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        chatMemory.add(systemMessage(Prompts.SMART_HOME_ASST_SYS_MESSAGE));

        var assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(model)
                .tools(new CurrentInformationRetrieverService(),
                        new EnvironmentRecognizerService(),
                        new SmartOutletManagerService())
                .chatMemory(chatMemory)
                //.contentRetriever(DocumentRetriever.get())
                .build();

        var intentAssistant = AiServices.create(IntentControllerAssistant.class, model);

        List<String> queries = Arrays.asList(
                "Can you turn on the lights please?"
                , "What's the weather today?"
                , "Why does it feel hot today?"
//                , "Give me a brief view of the news today"
//                , "Why is the sky blue?"
//                , "What can you see?"
//                , "What's today again?"
//                , "Describe the room you are in"
//                , "What are Emilio Aguinaldo College's linkages?"
        );


        for(String s: queries) {
            System.out.println("[USER]: " + s + "\n[LLM]: " +
                    //intentAssistant.specifyIntent(s) +
                    assistant.chat(s) +
                    "\n");
        }
    }

}
