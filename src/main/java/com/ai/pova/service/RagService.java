package com.ai.pova.service;

import org.springframework.stereotype.Service;

@Service
public class RagService {
	
	private  RagQueryService ragQueryService;
	
	 public RagService(RagQueryService ragQueryService) {
	        this.ragQueryService = ragQueryService;
	    }


	public String search(String query) {
		return ragQueryService.ask(query);
		
	}
}
