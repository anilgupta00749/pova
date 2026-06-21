package com.ai.pova.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Incoming chat message from the client.
 *
 * @param conversationId  stable session ID — used to maintain chat history per user
 * @param message         the user's question
 */
public record ChatRequest(

        @NotBlank(message = "conversationId must not be blank")
        String conversationId,

        @NotBlank(message = "message must not be blank")
        @Size(max = 4000, message = "message must be 4000 characters or fewer")
        String message
) {}
