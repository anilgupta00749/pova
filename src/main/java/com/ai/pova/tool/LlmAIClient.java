package com.ai.pova.tool;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

@Component
public class LlmAIClient {


    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.base.url}")
    private String baseUrl;


    public String getSearchIntent(String input) {
    	
        try {
        	
            String prompt = PromptBuilder.buildIntentPrompt(input);
            Client client = new Client.Builder()
                    .apiKey("AIzaSyCwDuKC5ziBtZrgxRJnzF2KlB9og_Ey26s")
                    .build();
            GenerateContentResponse response =
                    client.models.generateContent(
                        "gemini-3-flash-preview",
                        prompt,
                        null);
           
        
            return response.text();

        } catch (Exception e) {
            throw new RuntimeException("Error calling OpenAI: " + e.getMessage(), e);
        }
    }
    
}