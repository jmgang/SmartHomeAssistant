package org.assistant.assistant.sample;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.UrlDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.transformer.HtmlTextExtractor;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.UserMessage;

import static java.time.Duration.ofSeconds;

public class JavaNewsRetrieverToolsExample {
    static class JavaNewsRetriever {

        @Tool("Retrieves the latest java news. Strictly limit to the 3 latest news")
        String retrieveJavaNews() {
            Document javaNews = UrlDocumentLoader.load("https://dev.java/news/", new TextDocumentParser());
            Document transformedJavaNews = new HtmlTextExtractor(".container", null, true)
                    .transform(javaNews);

            return transformedJavaNews.text().replaceAll("\n", " ");
        }
    }

    interface Assistant {
        String chat(String userMessage);
    }

    interface NewsPrettierAssistant {

        @UserMessage("Given a jumbled java news {{it}}, summarize each, and list down them in numerical format, latest to oldest. " +
                "Include details such as url and date announced")
        String prettify(String userMessage);
    }

    public static void main(String[] args) {
        var model = OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .timeout(ofSeconds(200))
                .build();

        var assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(model)
                .tools(new JavaNewsRetriever())
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();

        var newsPrettierAssistant = AiServices.builder(NewsPrettierAssistant.class)
                .chatLanguageModel(model)
                .build();

        var question = "What are latest java news?";

        var answer = assistant.chat(question);

        var prettiedAnswer = newsPrettierAssistant.prettify(answer);

        System.out.println("\n=================================\n"+prettiedAnswer);
    }
}
