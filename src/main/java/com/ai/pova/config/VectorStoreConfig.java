package com.ai.pova.config;

import org.springframework.ai.google.genai.GoogleGenAiEmbeddingConnectionDetails;         // ✅ correct
import org.springframework.ai.google.genai.text.GoogleGenAiTextEmbeddingModel;             // ✅ correct
import org.springframework.ai.google.genai.text.GoogleGenAiTextEmbeddingOptions;           // ✅ correct
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VectorStoreConfig {

    @Value("${spring.ai.google.genai.api-key}")
    private String apiKey;

    @Bean
    public GoogleGenAiEmbeddingConnectionDetails googleGenAiEmbeddingConnectionDetails() {
        return GoogleGenAiEmbeddingConnectionDetails.builder()
                .apiKey(apiKey)   // API key only — no projectId, no location
                .build();
    }

    @Bean
    public GoogleGenAiTextEmbeddingModel googleGenAiTextEmbeddingModel(
            GoogleGenAiEmbeddingConnectionDetails connectionDetails) {

        GoogleGenAiTextEmbeddingOptions options =
                GoogleGenAiTextEmbeddingOptions.builder()
                        .model("gemini-embedding-001")
                        .build();

        return new GoogleGenAiTextEmbeddingModel(connectionDetails, options);
    }

    @Bean
    public SimpleVectorStore vectorStore(GoogleGenAiTextEmbeddingModel embeddingModel) {
        return SimpleVectorStore.builder(embeddingModel).build();
    }
}