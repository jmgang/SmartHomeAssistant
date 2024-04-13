package org.assistant.assistant.rag;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.weaviate.WeaviateEmbeddingStore;

import static org.assistant.config.ConfigLoader.getProperty;

public class EmbeddingStore {

    private static final dev.langchain4j.store.embedding.EmbeddingStore<TextSegment> embeddingStore = WeaviateEmbeddingStore.builder()
            .apiKey(getProperty("assistant.weaviate.apikey"))
            .scheme("https")
            .host(getProperty("assistant.weaviate.host"))
            .avoidDups(true)
            .consistencyLevel("ALL")
            .build();

    public static dev.langchain4j.store.embedding.EmbeddingStore<TextSegment> get() {
        return embeddingStore;
    }
}
