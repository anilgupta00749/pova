package com.ai.pova.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Central AI configuration for Pova.
 *
 * Sprint 1: wires ChatClient with system prompt and in-memory conversation memory.
 * Sprint 2+: add VectorStore RAG advisor here.
 * Sprint 5+: swap InMemoryChatMemory for RedisChatMemory.
 */
@Configuration
public class PovaAiConfig {
	/*
	 * 
	 * private static final String SYSTEM_PROMPT = """ You are Pova, an intelligent
	 * support assistant for the Producer Onboarding team.
	 * 
	 * You help support agents and producers by answering questions about: -
	 * Onboarding processes, policies, and procedures (from Confluence and
	 * SharePoint) - Business data and metrics (via internal database queries) -
	 * Pipeline errors and log analysis (via ADX/Kusto)
	 * 
	 * Guidelines: - Be concise and specific. Support agents need fast, actionable
	 * answers. - Always cite the source of your information (page name, table name,
	 * log query). - If you are unsure, say so clearly rather than guessing. - For
	 * data queries, confirm the parameters (date range, producer ID, region) before
	 * querying. - Format tabular results as readable markdown tables. """;
	 * 
	 * @Bean public InMemoryChatMemory chatMemory() { return new
	 * InMemoryChatMemory(); }
	 * 
	 * @Bean public ChatClient chatClient(ChatClient.Builder builder,
	 * InMemoryChatMemory chatMemory) { return builder .defaultSystem(SYSTEM_PROMPT)
	 * .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory)) .build(); }
	 */}
