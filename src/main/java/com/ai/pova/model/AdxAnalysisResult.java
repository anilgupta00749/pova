package com.ai.pova.model;

import java.util.List;
import java.util.Map;

public class AdxAnalysisResult {
	
private String userQuestion;
private String appName;
private String executedKql;
private List<Map<String, Object>> rawResults;
private int rowCount;
private String interpretation;
private boolean success;
private String error;

public String getUserQuestion() { return userQuestion; }
public void setUserQuestion(String userQuestion) { this.userQuestion = userQuestion; }

public String getAppName() { return appName; }
public void setAppName(String appName) { this.appName = appName; }

public String getExecutedKql() { return executedKql; }
public void setExecutedKql(String executedKql) { this.executedKql = executedKql; }

public List<Map<String, Object>> getRawResults() { return rawResults; }
public void setRawResults(List<Map<String, Object>> rawResults) { this.rawResults = rawResults; }

public int getRowCount() { return rowCount; }
public void setRowCount(int rowCount) { this.rowCount = rowCount; }

public String getInterpretation() { return interpretation; }
public void setInterpretation(String interpretation) { this.interpretation = interpretation; }

public boolean isSuccess() { return success; }
public void setSuccess(boolean success) { this.success = success; }

public String getError() { return error; }
public void setError(String error) { this.error = error; }

@Override
public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append("=== ADX Analysis Result ===").append("\n\n");

    sb.append("Question    : ").append(userQuestion).append("\n");
    sb.append("Application : ").append(appName).append("\n");
    sb.append("Status      : ").append(success ? "✅ Success" : "❌ Failed").append("\n");
    sb.append("Row Count   : ").append(rowCount).append("\n\n");

    sb.append("--- Executed KQL ---").append("\n");
    sb.append(executedKql != null ? executedKql.trim() : "N/A").append("\n\n");

    if (!success && error != null) {
        sb.append("--- Error ---").append("\n");
        sb.append(error).append("\n\n");
    }

    if (rawResults != null && !rawResults.isEmpty()) {
        sb.append("--- Raw Results (").append(rowCount).append(" rows) ---").append("\n");
        // Header
        sb.append(String.join(" | ", rawResults.get(0).keySet())).append("\n");
        sb.append("-".repeat(80)).append("\n");
        // Rows
        for (Map<String, Object> row : rawResults) {
            sb.append(String.join(" | ",
                    row.values().stream()
                            .map(Object::toString)
                            .toList()
            )).append("\n");
        }
        sb.append("\n");
    }

    sb.append("--- Interpretation ---").append("\n");
    sb.append(interpretation != null ? interpretation : "N/A").append("\n");

    return sb.toString();
}
}
