package com.ai.pova.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.tika.Tika;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import java.io.File;

import com.ai.pova.model.RagDocument;
import com.ai.pova.model.RagSource;

@Component
public class PdfDocumentLoader implements DocumentLoader {

    @Override
    public boolean supports(String type) {
        return "pdf".equalsIgnoreCase(type);
    }

    @Override
    public List<RagDocument> load(RagSource source) {
    	 List<RagDocument> result = new ArrayList<>();

    	try {
    	   // Resource resource = new FileSystemResource(source.getPath());
    		Resource resource = new FileSystemResource("C:/Users/LENOVO/Downloads/pova/pova/docs/sample.pdf");
    		

    	    if (!resource.exists()) {
    	        //log.error("PDF resource not found: {}", resource.getFile().getAbsolutePath());
    	        
    	        return result;
    	    }

    	    //log.info("Reading PDF: {}", resource.getFile().getAbsolutePath());

    	    // Spring AI's built-in PDF reader — handles text extraction properly
    	    PdfDocumentReaderConfig config = PdfDocumentReaderConfig.builder()
                    .withPageTopMargin(0)
                    .withPageBottomMargin(0)
                    .withPagesPerDocument(1)
                    .build();

    	    PagePdfDocumentReader pdfReader =
    	            new PagePdfDocumentReader(resource, config);

    	    List<Document> pages = pdfReader.get();

    	   // log.info("PDF pages extracted: {}", pages.size());

    	    // Combine all pages into one RagDocument
    	    StringBuilder fullContent = new StringBuilder();
    	    for (Document page : pages) {
    	        String text = page.getText();
    	        if (text != null && !text.isBlank()) {
    	            fullContent.append(text).append("\n\n");
    	        }
    	    }

    	    String content = fullContent.toString().trim();
    	   System.out.println("pdf content:   "+content);

    	    if (content.isBlank()) {
    	       // log.error("PDF text extraction returned EMPTY — PDF may be image/scanned: {}", source.getPath());
    	        return result;
    	    }

    	    RagDocument ragDocument = new RagDocument();
    	    ragDocument.setTitle(source.getName());
    	    ragDocument.setContent(content);
    	    ragDocument.setMetadata(java.util.Map.of(
    	            "source", source.getPath(),
    	            "type", source.getType()
    	    ));

    	    result.add(ragDocument);

    	} catch (Exception e) {
    	   // log.error("Failed to load PDF: {} | error: {}", source.getPath(), e.getMessage(), e);
    	}
    	return result;
    }

	
}




