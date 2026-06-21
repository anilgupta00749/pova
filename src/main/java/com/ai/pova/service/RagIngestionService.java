package com.ai.pova.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;


import com.ai.pova.config.RagProperties;

import com.ai.pova.model.RagDocument;
import com.ai.pova.model.RagSource;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RagIngestionService {

    public RagIngestionService(RagProperties ragProperties, List<DocumentLoader> loaders,
			ChunkingService chunkingService, VectorStore vectorStore) {
		super();
		this.ragProperties = ragProperties;
		this.loaders = loaders;
		this.chunkingService = chunkingService;
		this.vectorStore = vectorStore;
	}

	private  RagProperties ragProperties;

    private  List<DocumentLoader> loaders;

    private  ChunkingService chunkingService;

    private  VectorStore vectorStore;

    @PostConstruct
    public void ingestDocuments() {
    	
		
		  List<RagSource> sources = new ArrayList(); RagSource s1 = new RagSource();
		 /* s1.setName("underwriting-guide"); s1.setType("pdf");
		 * s1.setPath("./docs/abcd.pdf"); sources.add(s1);
		 * this.ragProperties.setSources(sources);
		 */
    	
    	RagSource confluence = new RagSource();
        confluence.setName("confluence-page");
        confluence.setType("confluence");
        sources.add(confluence);

        this.ragProperties.setSources(sources);

        for (RagSource source : ragProperties.getSources()) {

            DocumentLoader loader = loaders.stream()
                    .filter(l -> l.supports(source.getType()))
                    .findFirst()
                    .orElseThrow();

            List<RagDocument> documents =
                    loader.load(source);
            
            if (documents == null || documents.isEmpty()) {
            	System.out.println("LOADER RETURNED NO DOCUMENTS for source:"+ source.getPath());
               
                continue;
            }

            for (RagDocument document : documents) {
            	 String content = document.getContent();
            	 if (content == null || content.isBlank()) {
            			System.out.println("EMPTY CONTENT for document:"+ document.getTitle());
                     continue;
                 }
                List<String> chunks =
                        chunkingService.chunk(
                        		content
                        );
                if (chunks == null || chunks.isEmpty()) {
                	System.out.println("CHUNKING RETURNED NO CHUNKS for document: "+document.getTitle());
                    continue;
                }

                List<Document> vectorDocs =
                        chunks.stream()
                                .map(chunk -> {

                                    Map<String, Object> metadata =
                                            new HashMap<>();

                                    metadata.put(
                                            "title",
                                            document.getTitle()
                                    );

                                    metadata.putAll(
                                            document.getMetadata()
                                    );

                                    return new Document(
                                            chunk,
                                            metadata
                                    );

                                }).toList();

                vectorStore.add(vectorDocs);

                System.out.println(
                        "Indexed: "
                                + document.getTitle()
                                + " Chunks: "
                                + vectorDocs.size()
                );
            }
        }
    }

}