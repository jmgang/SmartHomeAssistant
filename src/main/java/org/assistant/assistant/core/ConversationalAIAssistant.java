package org.assistant.assistant.core;

import dev.langchain4j.chain.ConversationalChain;
import dev.langchain4j.data.document.loader.UrlDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.transformer.HtmlTextExtractor;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.assistant.stt.StreamingSpeechTranscriber;
import org.assistant.stt.TranscriptionResultHandler;

import java.util.Arrays;
import java.util.Collections;

import static dev.langchain4j.data.message.SystemMessage.systemMessage;
import static java.time.Duration.ofSeconds;

public class ConversationalAIAssistant {

    private final ChatLanguageModel model;

    private final ConversationalChain chain;

    public ConversationalAIAssistant() {
        model = OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .timeout(ofSeconds(120))
                .build();

        MessageWindowChatMemory memory = MessageWindowChatMemory.withMaxMessages(7);
        memory.add(systemMessage("You are a friendly conversational assistant named Melrose, who is talking to another person. The person may have spelling or grammar mistakes so infer the meaning " +
                "the best you can. The person can either converse or ask you questions. Your job is just to converse. Always limit your response to 2 lines max."));

        chain = ConversationalChain.builder()
                .chatLanguageModel(model)
                .chatMemory(memory)
                .build();
    }

    public String chat(String query) {
        return chain.execute(query);
    }
}
