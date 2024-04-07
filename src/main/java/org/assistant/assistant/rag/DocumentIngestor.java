package org.assistant.assistant.rag;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.loader.UrlDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.document.transformer.HtmlTextExtractor;
import dev.langchain4j.model.embedding.bge.small.en.v15.BgeSmallEnV15QuantizedEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.weaviate.WeaviateEmbeddingStore;

import static dev.langchain4j.model.openai.OpenAiModelName.GPT_3_5_TURBO;

public class DocumentIngestor {

    public static void main(String[] args) {


        // 2. Load an example document ("Miles of Smiles" terms of use)
        Document document = UrlDocumentLoader.load("https://www.eac.edu.ph/research-and-development-office/",
                new TextDocumentParser());

        Document transformedDocument = new HtmlTextExtractor().transform(document);

        System.out.println(transformedDocument);

        var embeddingModel = new BgeSmallEnV15QuantizedEmbeddingModel();

        // 3. Split the document into segments 100 tokens each
        // 4. Convert segments into embeddings
        // 5. Store embeddings into embedding store
        // All this can be done manually, but we will use EmbeddingStoreIngestor to automate this:
        DocumentSplitter documentSplitter = DocumentSplitters.recursive(100, 0,
                new OpenAiTokenizer(GPT_3_5_TURBO));
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(documentSplitter)
                .embeddingModel(embeddingModel)
                .embeddingStore(EmbeddingStore.get())
                .build();
        ingestor.ingest(transformedDocument);
    }
}
