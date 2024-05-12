

package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.AlanChatResponse;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Objects;

@Service
@Slf4j
public class AlanChatService {

    private final WebClient webClient;
    private final String clientId;
    private final ObjectMapper objectMapper;

    public AlanChatService(WebClient.Builder webClientBuilder,
                           @Value("${alan.client-id}") String clientId) {
        this.webClient = webClientBuilder.baseUrl("https://kdt-api-function.azurewebsites.net").build();
        this.clientId = clientId;
        this.objectMapper = new ObjectMapper()
                .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    }

    public Flux<ServerSentEvent<AlanChatResponse>> streamAlanResponse(String content) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/question/sse-streaming")
                        .queryParam("content", content)
                        .queryParam("client_id", clientId)
                        .build())
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class)
                .map(responseString -> {
                    try {
                        return objectMapper.readValue(responseString, AlanChatResponse.class);
                    } catch (Exception e) {
                        log.error("Alan response 파싱 에러", e);
                        return new AlanChatResponse();
                    }
                })
                .map(response -> {
                    if (response != null) {
                        return ServerSentEvent.builder(response).build();
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .delayElements(Duration.ofMillis(5));
    }
}
