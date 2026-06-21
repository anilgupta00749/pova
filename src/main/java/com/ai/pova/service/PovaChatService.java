package com.ai.pova.service;

import com.ai.pova.model.ChatRequest;
import com.ai.pova.model.ChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * Core chat service for POVA.
 *
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PovaChatService {
	/*
	 * 
	 * private final ChatClient chatClient;
	 * 
	 * // ── Blocking chat ──────────────────────────────────────────────────────
	 * 
	 * public ChatResponse chat(ChatRequest request) {
	 * log.info("POVA [session={}] question: {}", request.conversationId(),
	 * request.message());
	 * 
	 * String answer = chatClient .prompt() .user(request.message())
	 * .advisors(advisor -> advisor
	 * .param(MessageChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY,
	 * request.conversationId())
	 * .param(MessageChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY, 20) ) .call()
	 * .content();
	 * 
	 * log.info("POVA [session={}] answered: {} chars", request.conversationId(),
	 * answer == null ? 0 : answer.length());
	 * 
	 * return ChatResponse.of(request.conversationId(), answer); }
	 * 
	 * // ── Streaming chat (Server-Sent Events) ───────────────────────────────
	 * 
	 * public Flux<String> stream(ChatRequest request) {
	 * log.info("POVA [session={}] streaming question: {}",
	 * request.conversationId(), request.message());
	 * 
	 * return chatClient .prompt() .user(request.message()) .advisors(advisor ->
	 * advisor .param(MessageChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY,
	 * request.conversationId())
	 * .param(MessageChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY, 20) )
	 * .stream() .content(); }
	 */}
