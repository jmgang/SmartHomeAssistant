package org.assistant.assistant.rag;

import dev.langchain4j.model.embedding.bge.small.en.v15.BgeSmallEnV15QuantizedEmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;

public class DocumentRetriever {

    public static ContentRetriever get() {
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(EmbeddingStore.get())
                .embeddingModel(new BgeSmallEnV15QuantizedEmbeddingModel())
                .maxResults(3) // on each interaction we will retrieve the 2 most relevant segments
                .minScore(0.8) // we want to retrieve segments at least somewhat similar to user query
                .build();
    }

}
