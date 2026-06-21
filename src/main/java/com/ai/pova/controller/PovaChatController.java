package com.ai.pova.controller;

import com.ai.pova.model.ChatRequest;
import com.ai.pova.model.ChatResponse;
import com.ai.pova.service.OrchestratorService;
import com.ai.pova.service.PovaChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * REST controller exposing POVA's chat API.
 *
 * POST /api/v1/chat        — send a message, receive a single JSON reply
 * POST /api/v1/chat/stream — same, but streams tokens via Server-Sent Events
 * GET  /api/v1/chat/health — simple liveness check
 */
@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class PovaChatController {

    @Autowired
	private  OrchestratorService orchestratorService;

    /**
     * Blocking chat — waits for the full response before returning.
     * Good for programmatic clients that need the complete answer in one shot.
     */
    //@PostMapping
    //public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
    //    return ResponseEntity.ok(chatService.chat(request));
   // }
    
    @PostMapping
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        return ResponseEntity.ok(orchestratorService.process(request));
    }

    /**
     * Streaming chat via Server-Sent Events.
     * Tokens are pushed to the client as they arrive from OpenAI,
     * giving a responsive "typing" feel in the UI.
     *
     * Usage: EventSource or fetch() with ReadableStream on the frontend.
     */
   // @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
   // public Flux<String> stream(@Valid @RequestBody ChatRequest request) {
   //     return chatService.stream(request);
   // }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("POVA is online");
    }
}
