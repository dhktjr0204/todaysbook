package com.example.todaysbook.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class AlanChatbotService {

    private final WebClient webClient;
    private final String clientId;

    public AlanChatbotService(WebClient.Builder webClientBuilder, @Value("${alan.api.url}") String baseUrl, @Value("${alan.api.client-id}") String clientId) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.clientId = clientId;
    }

    public Flux<String> getQuestionResponse(String content) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/question/sse-streaming")
                        .queryParam("content", content)
                        .queryParam("client_id", clientId)
                        .build())
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class);
    }
}