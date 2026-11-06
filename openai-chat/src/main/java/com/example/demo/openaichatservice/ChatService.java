package com.example.demo.openaichatservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class ChatService {

    private final RestClient restClient;
    private final String apiKey;
    private final String model;

    public ChatService(
            @Value("${openai.api-key}") String apiKey,
            @Value("${openai.model}") String model) {

        this.restClient = RestClient.create();
        this.apiKey = apiKey;
        this.model = model;
    }

    public String chat(String message) {

        String response = restClient.post()
                .uri("https://api.openai.com/v1/responses")
                .header("Authorization", "Bearer " + apiKey)
                .body(Map.of(
                        "model", model,
                        "input", message
                ))
                .retrieve()
                .body(String.class);

        try {

            ObjectMapper mapper = new ObjectMapper();

            JsonNode root = mapper.readTree(response);

            return root.path("output")
                    .get(0)
                    .path("content")
                    .get(0)
                    .path("text")
                    .asText();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to parse OpenAI response", e);
        }
    }
}