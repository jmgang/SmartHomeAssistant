package org.assistant.assistant.sample;

import dev.langchain4j.chain.ConversationalRetrievalChain;
import dev.langchain4j.data.document.loader.UrlDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.document.transformer.HtmlTextExtractor;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.retriever.EmbeddingStoreRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.weaviate.WeaviateEmbeddingStore;

import java.util.Arrays;

import static java.time.Duration.ofSeconds;

public class ConversationRAGChatbotExample {

    public static void main(String[] args) {

        var document = UrlDocumentLoader
                .load("https://lspu.edu.ph/page/historical-development", new TextDocumentParser());

        var textExtractor = new HtmlTextExtractor();
        var transformedDocument = textExtractor.transform(document);

//        System.out.println(transformedDocument);

        EmbeddingStore<TextSegment> embeddingStore = WeaviateEmbeddingStore.builder()
                .apiKey(System.getenv("WEAVIATE_API_KEY"))
                .scheme("https")
                .host("langchain4j-demo-36fruowh.weaviate.network")
                .avoidDups(true)
                .consistencyLevel("ALL")
                .build();

        EmbeddingModel embeddingModel = OpenAiEmbeddingModel
                .builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName("text-embedding-3-small")
                .build();

        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(500, 0))
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        ingestor.ingest(transformedDocument);

        var model = OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .timeout(ofSeconds(120))
                .build();

        ConversationalRetrievalChain chain = ConversationalRetrievalChain.builder()
                .chatLanguageModel(model)
                .retriever(EmbeddingStoreRetriever.from(embeddingStore, embeddingModel))
//                 .chatMemory(MessageWindowChatMemory.withMaxMessages(12))
                 .promptTemplate(PromptTemplate.from(
                         "Answer the following question to the best of your ability: {{question}}\n\n" +
                         "You can base your answer on the following information:\n{{information}}. Limit your response to 2 lines max."))
                .build();

        var userMessages = Arrays.asList(
                "When was the Baybay Provincial High School converted into Baybay National Agricultural and Vocational School (BNAVS)?",
                "What degree courses were offered by BNCAT after its conversion in 1971?",
                "Who was the first College President of Laguna State Polytechnic College?",
                "How many graduates did the PACD-BVE Training Center produce in rice production for American Peace Corps Volunteers?"
        );
        var output = new StringBuilder();

        System.out.println("============================================");
        for (String userMessage : userMessages) {
            output.append("\n[USER]: ")
                    .append(userMessage)
                    .append("\n[LLM]: ")
                    .append(chain.execute(userMessage))
                    .append("\n");
        }

        System.out.println(output);
    }
}
