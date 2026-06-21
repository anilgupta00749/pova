package com.ai.pova.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.ai.pova.model.RagSource;

@Configuration
@ConfigurationProperties(prefix = "rag")

public class RagProperties {

    private List<RagSource> sources =
            new ArrayList<>();

    public List<RagSource> getSources() {
        return sources;
    }

    public void setSources(List<RagSource> sources) {
        this.sources = sources;
    }
}
