package com.ai.pova.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "adx")
public class AdxProperties {

    private String clusterUrl;
    private String database;
    private String clientId;
    private String clientSecret;
    private String tenantId;
    private int maxRows = 100;

    public String getClusterUrl() { return clusterUrl; }
    public void setClusterUrl(String clusterUrl) { this.clusterUrl = clusterUrl; }

    public String getDatabase() { return database; }
    public void setDatabase(String database) { this.database = database; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public String getClientSecret() { return clientSecret; }
    public void setClientSecret(String clientSecret) { this.clientSecret = clientSecret; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public int getMaxRows() { return maxRows; }
    public void setMaxRows(int maxRows) { this.maxRows = maxRows; }
}