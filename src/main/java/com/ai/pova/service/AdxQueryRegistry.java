package com.ai.pova.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class AdxQueryRegistry {

    // Key: application name (lowercase), Value: KQL query
    private final Map<String, String> queryMap = new HashMap<>();

    public AdxQueryRegistry() {

        queryMap.put("payment-service",
                """
                AppLogs
                | where Timestamp > ago(2h)
                | where ServiceName == 'payment-service'
                | where Level in ('ERROR', 'WARN')
                | project Timestamp, Level, Message, ExceptionType, StackTrace, CorrelationId
                | order by Timestamp desc
                | take 50
                """);

        queryMap.put("order-service",
                """
                AppLogs
                | where Timestamp > ago(2h)
                | where ServiceName == 'order-service'
                | where Level in ('ERROR', 'WARN')
                | project Timestamp, Level, Message, ExceptionType, CorrelationId
                | order by Timestamp desc
                | take 50
                """);

        queryMap.put("inventory-service",
                """
                AppLogs
                | where Timestamp > ago(2h)
                | where ServiceName == 'inventory-service'
                | where Level in ('ERROR', 'WARN')
                | project Timestamp, Level, Message, ExceptionType, CorrelationId
                | order by Timestamp desc
                | take 50
                """);

        queryMap.put("auth-service",
                """
                AppLogs
                | where Timestamp > ago(1h)
                | where ServiceName == 'auth-service'
                | where Level == 'ERROR'
                | project Timestamp, Level, Message, ExceptionType, UserId, CorrelationId
                | order by Timestamp desc
                | take 50
                """);
    }

    /**
     * Get KQL query for a given application name.
     * Returns null if no query is registered for the app.
     */
    public String getQuery(String appName) {
        return queryMap.get(appName.toLowerCase().trim());
    }

    /**
     * Check if an application name has a registered query.
     */
    public boolean hasQuery(String appName) {
        return queryMap.containsKey(appName.toLowerCase().trim());
    }

    /**
     * List all registered application names.
     */
    public java.util.Set<String> getRegisteredApps() {
        return queryMap.keySet();
    }
}