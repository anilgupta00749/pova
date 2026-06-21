package com.ai.pova.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.stream.Collectors;

/**
 * Centralised error handling for POVA's REST API.
 *
 * Returns RFC-9457 ProblemDetail responses so clients always get a consistent
 * JSON error shape regardless of the failure type.
 */
@Slf4j
@RestControllerAdvice
public class PovaExceptionHandler {
	/*
	
	*//** Validation failures (e.g. blank conversationId, message too long). */
	/*
	 * @ExceptionHandler(MethodArgumentNotValidException.class) public ProblemDetail
	 * handleValidation(MethodArgumentNotValidException ex) { String detail =
	 * ex.getBindingResult().getFieldErrors().stream()
	 * .map(FieldError::getDefaultMessage) .collect(Collectors.joining("; "));
	 * 
	 * ProblemDetail problem =
	 * ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
	 * problem.setTitle("Invalid request");
	 * problem.setType(URI.create("https://pova.internal/errors/validation"));
	 * return problem; }
	 * 
	 *//** OpenAI / Spring AI call failures. */
	/*
	 * @ExceptionHandler(org.springframework.ai.retry.NonTransientAiException.class)
	 * public ProblemDetail
	 * handleAiError(org.springframework.ai.retry.NonTransientAiException ex) {
	 * log.error("AI provider error: {}", ex.getMessage()); ProblemDetail problem =
	 * ProblemDetail.forStatusAndDetail( HttpStatus.BAD_GATEWAY,
	 * "The AI provider returned an error. Please try again.");
	 * problem.setTitle("AI provider error");
	 * problem.setType(URI.create("https://pova.internal/errors/ai-provider"));
	 * return problem; }
	 * 
	 *//** Catch-all for unexpected errors — never leak stack traces. */
	/*
	 * @ExceptionHandler(Exception.class) public ProblemDetail
	 * handleGeneric(Exception ex) { log.error("Unexpected error in POVA", ex);
	 * ProblemDetail problem = ProblemDetail.forStatusAndDetail(
	 * HttpStatus.INTERNAL_SERVER_ERROR,
	 * "An unexpected error occurred. The team has been notified.");
	 * problem.setTitle("Internal error");
	 * problem.setType(URI.create("https://pova.internal/errors/internal")); return
	 * problem; }
	 */}
