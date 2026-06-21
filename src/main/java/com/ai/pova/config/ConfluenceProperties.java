package com.ai.pova.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "confluence")
public class ConfluenceProperties {

    private String baseUrl;
    private String username;
    private String apiToken;
    private String spaceKey;
    private List<String> pageIds;

    public List<String> getPageIds() {
		return pageIds;
	}
	public void setPageIds(List<String> pageIds) {
		this.pageIds = pageIds;
	}
	public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getApiToken() { return apiToken; }
    public void setApiToken(String apiToken) { this.apiToken = apiToken; }

    public String getSpaceKey() { return spaceKey; }
    public void setSpaceKey(String spaceKey) { this.spaceKey = spaceKey; }
}