package com.ai.pova.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ai.pova.config.AdxProperties;
import com.microsoft.azure.kusto.data.Client;
import com.microsoft.azure.kusto.data.KustoOperationResult;
import com.microsoft.azure.kusto.data.KustoResultColumn;
import com.microsoft.azure.kusto.data.KustoResultSetTable;

@Service
public class AdxQueryService {

    private static final Logger log = LoggerFactory.getLogger(AdxQueryService.class);

    private final Client adxClient;
    private final AdxProperties adxProperties;

    public AdxQueryService(Client adxClient, AdxProperties adxProperties) {
        this.adxClient = adxClient;
        this.adxProperties = adxProperties;
    }

    public List<Map<String, Object>> executeQuery(String kqlQuery) {

        List<Map<String, Object>> rows = new ArrayList<>();

        try {
            log.info("Executing KQL query: {}", kqlQuery);

            KustoOperationResult results = adxClient.execute(
                    adxProperties.getDatabase(),
                    kqlQuery
            );

            KustoResultSetTable table = results.getPrimaryResults();

           
            KustoResultColumn[] columns = table.getColumns();

            log.info("ADX returned {} column(s)", columns.length);

            int rowCount = 0;

            while (table.next() && rowCount < adxProperties.getMaxRows()) {

                Map<String, Object> row = new LinkedHashMap<>();

                for (KustoResultColumn column : columns) {
                    // Read by column name using getString()
                    String colName = column.getColumnName();
                    Object value = table.getObject(column.getOrdinal());
                    row.put(colName, value != null ? value.toString() : "null");
                }

                rows.add(row);
                rowCount++;
            }

            log.info("Fetched {} row(s) from ADX (max: {})",
                    rowCount, adxProperties.getMaxRows());

        } catch (Exception e) {
            log.error("ADX query execution failed: {}", e.getMessage(), e);
            throw new RuntimeException("ADX query failed: " + e.getMessage(), e);
        }

        return rows;
    }

    public String formatResultsAsText(List<Map<String, Object>> rows) {

        if (rows == null || rows.isEmpty()) {
            return "No results returned from the query.";
        }

        StringBuilder sb = new StringBuilder();

        // Header
        sb.append(String.join(" | ", rows.get(0).keySet())).append("\n");
        sb.append("-".repeat(80)).append("\n");

        // Rows
        for (Map<String, Object> row : rows) {
            sb.append(String.join(" | ",
                    row.values().stream()
                            .map(Object::toString)
                            .toList()
            )).append("\n");
        }

        return sb.toString();
    }
}