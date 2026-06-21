package com.ai.pova.model;

import java.time.Instant;

/**
 * Pova's reply to a chat message.
 *
 */
public record ChatResponse(
        String conversationId,
        String answer,
        String tool,
        Instant timestamp
) {
    public static ChatResponse of(String conversationId, String answer) {
        return new ChatResponse(conversationId, answer, null, Instant.now());
    }
}
