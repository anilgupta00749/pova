package com.ai.pova;

import org.springframework.ai.model.google.genai.autoconfigure.embedding.GoogleGenAiEmbeddingConnectionAutoConfiguration;
import org.springframework.ai.model.google.genai.autoconfigure.embedding.GoogleGenAiTextEmbeddingAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.ai.pova.config.RagProperties;


@EnableConfigurationProperties(RagProperties.class)
@SpringBootApplication(exclude = {
	    GoogleGenAiEmbeddingConnectionAutoConfiguration.class,
	    GoogleGenAiTextEmbeddingAutoConfiguration.class
	})
public class PovaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PovaApplication.class, args);
	}

}
