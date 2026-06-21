package com.ai.pova.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.pova.model.ChatRequest;
import com.ai.pova.model.ChatResponse;
import com.ai.pova.tool.LlmAIClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OrchestratorService {
	
	@Autowired
	RagService ragService;
	@Autowired
	DbService dbService;
	@Autowired
	AdxService adxService;
	@Autowired
	LlmAIClient aiClient;


	
	public ChatResponse process(ChatRequest request) {
		
		String intentJson = aiClient.getSearchIntent(request.message());
		ObjectMapper mapper = new ObjectMapper();
		String error;
		try {
			JsonNode node = mapper.readTree(intentJson);
			if(node == null) {
				 return ChatResponse.of(request.conversationId(), "Unable to get intent response");
			}
			  System.out.println("node..."+node);
			String intent = node.get("intent").asText(); //
			System.out.println("intent..."+intent);
			double confidence = node.get("confidence").asDouble();
			System.out.println("confidence..."+confidence);
			if(confidence < 0.7) {
				 return ChatResponse.of(request.conversationId(), "Please provide some more inputs or be specific with query");
			}
			
			switch(intent) {
			   case "LOG_ANALYSIS":
				   System.out.println("log analytics");
			        //return ChatResponse.of(request.conversationId(), adxService.search(""));
				   return ChatResponse.of(request.conversationId(),  adxService.analyze(request.message()).toString());
			   case "DATABASE_QUERY":
				   System.out.println("db query");
			       //return ChatResponse.of(request.conversationId(),  dbService.search(""));
				   return ChatResponse.of(request.conversationId(),  ragService.search(request.message()));
			   case "DOCUMENT_SEARCH":
				   System.out.println("document search");
			       return ChatResponse.of(request.conversationId(),  ragService.search(request.message()));
			    default:
			    	return ChatResponse.of(request.conversationId(), "Refine your search with more info and specific details");
			}
			
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			error = e.getMessage();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			error = e.getMessage();
		} //
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			error = e.getMessage();
		}
		return ChatResponse.of(request.conversationId(),  error);


		
	}
	
}
 