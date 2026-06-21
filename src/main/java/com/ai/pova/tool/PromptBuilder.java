package com.ai.pova.tool;

public class PromptBuilder {

    public static String buildIntentPrompt(String input) {
        return  "You are an intent classification engine.Classify the user request into one of these intents:1. DOCUMENT_SEARCH 2. DATABASE_QUERY 3. LOG_ANALYSIS 4. MULTI_STEP 5. UNKNOWN\r\n"
        		+ "Rules:\r\n"
        		+ "- Questions about policy/process/business meaning => DOCUMENT_SEARCH\r\n"
        		+ "- Questions asking counts, reports, customer data => DATABASE_QUERY\r\n"
        		+ "- Questions about failures/errors/exceptions/debugging => LOG_ANALYSIS\r\n"
        		+ "- If multiple intents required => MULTI_STEP\r\n"
        		+ "\r\n"
        		+ "Return ONLY JSON.\r\n"
        		+ "\r\n"
        		+ "Example:{\"intent\": \"DATABASE_QUERY\",\"confidence\": 0.92} User Input:" + input;
    }
    
    
    public static String buildGapAnalysisPrompt(String input) {
        return """
        You are a senior solution architect.
        Analyze the requirement and identify gaps.
        Return ONLY valid JSON in this format:
        {
          "missingRequirements": ["item1"],
          "ambiguities": ["item1"],
          "risks": ["item1"],
          "clarificationQuestions": ["question1"]
        }

        Rules:
        - No explanation outside JSON
        - Be precise and practical
        - Focus on real-world software delivery risks

        Requirement:
        """ + input;
    }
}
