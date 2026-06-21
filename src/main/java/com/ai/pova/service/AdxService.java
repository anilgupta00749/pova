package com.ai.pova.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.ai.pova.model.AdxAnalysisResult;

@Service
public class AdxService {
	
	private static final Logger log = LoggerFactory.getLogger(AdxService.class);


private final ChatClient chatClient;
private final AdxQueryService adxQueryService;
private final AdxQueryRegistry queryRegistry;

public AdxService(ChatClient chatClient,
                     AdxQueryService adxQueryService,
                     AdxQueryRegistry queryRegistry) {
    this.chatClient = chatClient;
    this.adxQueryService = adxQueryService;
    this.queryRegistry = queryRegistry;
}

/**
 * Step 1 — Extract application name from user's natural language input
 */
public String extractAppName(String userQuestion) {

    String registeredApps = String.join(", ", queryRegistry.getRegisteredApps());

    String prompt = """
            You are a helpful assistant. Extract the application or service name
            from the user's question below.
            
            Only return one of the following registered application names exactly
            as listed — nothing else, no explanation:
            %s
            
            If no matching application is found, return: UNKNOWN
            
            User Question: %s
            
            Application Name:
            """.formatted(registeredApps, userQuestion);

    String appName = chatClient.prompt()
            .user(prompt)
            .call()
            .content();

    appName = appName.trim().toLowerCase();
    log.info("Extracted app name: '{}'", appName);
    return appName;
}

/**
 * Step 2 — Interpret raw ADX results in plain English
 */
public String interpretResults(String userQuestion,
                               String appName,
                               String kqlQuery,
                               List<Map<String, Object>> results) {

    String formattedResults = adxQueryService.formatResultsAsText(results);

    String prompt = """
            You are a log analysis expert.
            
            A user asked: "%s"
            
            Application: %s
            
            KQL Query Executed:
            %s
            
            Query Results:
            %s
            
            Based on the results above, provide:
            1. A plain English summary of what the logs show
            2. Any errors or warnings found with their frequency
            3. Possible root causes for any errors
            4. Recommended next steps or fixes
            
            If no results were returned, say the logs are clean for this service.
            
            Analysis:
            """.formatted(userQuestion, appName, kqlQuery, formattedResults);

    return chatClient.prompt()
            .user(prompt)
            .call()
            .content();
}

/**
 * Full pipeline:
 * user question → extract app name → lookup KQL → execute → interpret
 */
public AdxAnalysisResult analyze(String userQuestion) {

    AdxAnalysisResult result = new AdxAnalysisResult();
    result.setUserQuestion(userQuestion);

    try {
        // Step 1: Extract app name from user question
        String appName = extractAppName(userQuestion);
        result.setAppName(appName);

        // Step 2: Check registry for predefined query
        if ("unknown".equals(appName) || !queryRegistry.hasQuery(appName)) {
            log.warn("No predefined query found for app: '{}'", appName);
            result.setSuccess(false);
            result.setError(
                "No query registered for application: '" + appName + "'. " +
                "Registered apps are: " + queryRegistry.getRegisteredApps()
            );
            return result;
        }

        // Step 3: Get predefined KQL from registry
        String kqlQuery = queryRegistry.getQuery(appName);
        result.setExecutedKql(kqlQuery);
        log.info("Using predefined KQL for app '{}': {}", appName, kqlQuery);

        // Step 4: Execute KQL against ADX
        List<Map<String, Object>> queryResults =
                adxQueryService.executeQuery(kqlQuery);
        result.setRawResults(queryResults);
        result.setRowCount(queryResults.size());

        // Step 5: Interpret results with LLM
        String interpretation = interpretResults(
                userQuestion, appName, kqlQuery, queryResults);
        result.setInterpretation(interpretation);
        result.setSuccess(true);

    } catch (Exception e) {
        log.error("ADX analysis failed: {}", e.getMessage(), e);
        result.setSuccess(false);
        result.setError(e.getMessage());
    }

    return result;
}
}
