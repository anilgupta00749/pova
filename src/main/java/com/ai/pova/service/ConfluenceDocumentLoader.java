package com.ai.pova.service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.ai.pova.config.ConfluenceProperties;
import com.ai.pova.model.RagDocument;
import com.ai.pova.model.RagSource;
import com.fasterxml.jackson.databind.JsonNode;

@Component
public class ConfluenceDocumentLoader implements DocumentLoader {

    private static final Logger log = LoggerFactory.getLogger(ConfluenceDocumentLoader.class);

    private final WebClient webClient;
    private final ConfluenceProperties confluenceProperties;
    private final String basicAuth;

    public ConfluenceDocumentLoader(ConfluenceProperties confluenceProperties) {
        this.confluenceProperties = confluenceProperties;
        String credentials = confluenceProperties.getUsername().trim()
                + ":" + confluenceProperties.getApiToken().trim();
        this.basicAuth = "Basic " + Base64.getEncoder()
                .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        //String credentials = confluenceProperties.getUsername()
        //        + ":" + confluenceProperties.getApiToken();
        String basicAuth = "Basic " + Base64.getEncoder()
                .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(config -> config
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024)  // 16MB
                )
                .build();
        this.webClient = WebClient.builder()
                .baseUrl(confluenceProperties.getBaseUrl())
                .exchangeStrategies(exchangeStrategies)   
                .defaultHeader("Authorization", basicAuth)
                .defaultHeader("Accept", "application/json")
                .build();
    }

    @Override
    public boolean supports(String type) {
        return "confluence".equalsIgnoreCase(type);
    }

    @Override
    public List<RagDocument> load(RagSource source) {

        List<RagDocument> result = new ArrayList<>();

        // ✅ Read all page IDs from properties
        List<String> pageIds = confluenceProperties.getPageIds();

        if (pageIds == null || pageIds.isEmpty()) {
            log.warn("No Confluence page IDs configured under confluence.page-ids");
            return result;
        }

        log.info("Loading {} Confluence page(s)...", pageIds.size());

        for (String pageId : pageIds) {
            try {
                RagDocument doc = loadSinglePage(pageId);
                if (doc != null) {
                    result.add(doc);
                }
            } catch (Exception e) {
                // Don't stop — log and continue with remaining pages
                log.error("Failed to load page ID: {} | error: {}", pageId, e.getMessage(), e);
            }
        }

        log.info("✅ Successfully loaded {}/{} Confluence pages",
                result.size(), pageIds.size());

        return result;
    }

    private RagDocument loadSinglePage(String pageId) {

        log.info("Fetching Confluence page ID: {}", pageId);
        String responseBody;
        try {
        	responseBody = webClient.get()
                .uri("/wiki/rest/api/content/{pageId}?expand=body.storage", pageId)
                .header(HttpHeaders.AUTHORIZATION, basicAuth)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header("X-Atlassian-Token", "no-check")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        }catch (WebClientResponseException e) {
        	e.printStackTrace();
            log.error("HTTP {} error for page {}: {}", e.getStatusCode(), pageId, e.getResponseBodyAsString());
            return null;
        }

        if (responseBody == null) {
            log.error("Null response from Confluence for page: {}", pageId);
            return null;
        }
        com.fasterxml.jackson.databind.ObjectMapper mapper =
                new com.fasterxml.jackson.databind.ObjectMapper();
        JsonNode root;
        try {
            root = mapper.readTree(responseBody);
        } catch (Exception e) {
            log.error("Failed to parse JSON for page {}: {}", pageId, e.getMessage());
            log.error("Response was: {}", responseBody.substring(0, Math.min(500, responseBody.length())));
            return null;
        }

        String title = root.path("title").asText("Untitled");
        log.info("Page title: '{}'", title);

        String htmlContent = root
                .path("body")
                .path("storage")
                .path("value")
                .asText("");

        if (htmlContent.isBlank()) {
            log.error("Empty body for Confluence page: {}", pageId);
            return null;
        }

        // Strip HTML tags to plain text
        String plainText = Jsoup.parse(htmlContent).text();
        log.info("Page '{}' — extracted {} chars", title, plainText.length());

        if (plainText.isBlank()) {
            log.error("Plain text empty after HTML stripping for page: {}", pageId);
            return null;
        }

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("source", "confluence");
        metadata.put("pageId", pageId);
        metadata.put("title", title);
        metadata.put("url", confluenceProperties.getBaseUrl()
                + "/wiki/spaces/" + confluenceProperties.getSpaceKey()
                + "/pages/" + pageId);

        RagDocument ragDocument = new RagDocument();
        ragDocument.setTitle(title);
        ragDocument.setContent(plainText);
        ragDocument.setMetadata(metadata);

        return ragDocument;
    }
}