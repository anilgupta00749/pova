package com.ai.pova.tool;

/**
 * Tool function stubs — wired in via Spring AI function calling from Sprint 2 onward.
 *
 * Sprint 2: KnowledgeBaseSearchTool   — RAG over Confluence + SharePoint
 * Sprint 3: BusinessQueryTool         — intent → Java service → DB result
 * Sprint 4: AdxLogAnalysisTool        — intent → KQL → ADX → error summary
 *
 * Each tool will be a Spring @Bean of type java.util.function.Function<Input, Output>.
 * The ChatClient picks them up automatically when registered with .defaultTools(...).
 *
 * Example (Sprint 2):
 *
 *   @Bean
 *   @Description("Search the producer onboarding knowledge base for policies, procedures, and guides")
 *   public Function<KnowledgeBaseSearchTool.Request, KnowledgeBaseSearchTool.Response> searchKnowledgeBase(
 *           VectorStore vectorStore) {
 *       return new KnowledgeBaseSearchTool(vectorStore);
 *   }
 */
public final class PovaToolRegistry {
    private PovaToolRegistry() {}
}
