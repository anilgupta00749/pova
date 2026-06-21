package com.ai.pova.config;

import com.microsoft.azure.kusto.data.Client;
import com.microsoft.azure.kusto.data.ClientFactory;
import com.microsoft.azure.kusto.data.auth.ConnectionStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdxClientConfig {

    private static final Logger log = LoggerFactory.getLogger(AdxClientConfig.class);

    @Bean
    public Client adxClient(AdxProperties adxProperties) throws Exception {

        log.info("Initializing ADX client for cluster: {}", adxProperties.getClusterUrl());

        // App registration auth — client ID + secret
        ConnectionStringBuilder csb =
                ConnectionStringBuilder.createWithAadApplicationCredentials(
                        adxProperties.getClusterUrl(),
                        adxProperties.getClientId(),
                        adxProperties.getClientSecret(),
                        adxProperties.getTenantId()
                );

        return ClientFactory.createClient(csb);
    }
}