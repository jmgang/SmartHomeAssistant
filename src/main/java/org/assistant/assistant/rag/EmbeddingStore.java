package org.assistant.assistant.rag;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.weaviate.WeaviateEmbeddingStore;

public class EmbeddingStore {

    private static final dev.langchain4j.store.embedding.EmbeddingStore<TextSegment> embeddingStore = WeaviateEmbeddingStore.builder()
            .apiKey(System.getenv("WEAVIATE_API_KEY"))
            .scheme("https")
            .host("rpi-rag-sample-wscafsa5.weaviate.network")
            .avoidDups(true)
            .consistencyLevel("ALL")
            .build();

    public static dev.langchain4j.store.embedding.EmbeddingStore<TextSegment> get() {
        return embeddingStore;
    }
}
