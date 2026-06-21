package com.ai.pova.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;

import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;



@Service
public class RagQueryService {

    public RagQueryService(VectorStore vectorStore, ChatClient chatClient) {
		super();
		this.vectorStore = vectorStore;
		this.chatClient = chatClient;
	}

	private  VectorStore vectorStore;

    private  ChatClient chatClient;

    public String ask(String query) {

        List<Document> similarDocs =
                vectorStore.similaritySearch(
                        SearchRequest.builder()
                                .query(query)
                                .topK(5)
                                .build()
                );

        String context =
                similarDocs.stream()
                        .map(Document::getText)
                        .collect(Collectors.joining("\n"));

        String prompt = """
                You are a business assistant.

                Answer ONLY from the provided context.

                If answer is unavailable,
                say:
                "Information not found."

                Context:
                %s

                User Question:
                %s
                """.formatted(context, query);

        return chatClient.prompt(prompt)
                .call()
                .content();
    }
}
