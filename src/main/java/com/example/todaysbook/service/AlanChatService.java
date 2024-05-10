package com.example.todaysbook.service;


import com.example.todaysbook.domain.dto.AlanChatData;
import com.example.todaysbook.domain.dto.AlanChatResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class AlanChatService {

    private final WebClient webClient;
    private final String clientId;

    public AlanChatService(WebClient.Builder webClientBuilder, @Value("${alan.client-id}") String clientId) {
        this.webClient = webClientBuilder.baseUrl("https://kdt-api-function.azurewebsites.net").build();
        this.clientId = clientId;
    }

    public Flux<ServerSentEvent<String>> streamAlanResponse(String content) {
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
                    String eventType = extractEventType(responseString);
                    String data = extractData(responseString);
                    String contentString = extractContentString(data);
//                    log.info("eventType: {},        data: {}", eventType, data);
//                    log.info("data: {}", data);
                    log.info("contentString: {}", contentString);
                    return ServerSentEvent.<String>builder()
                            //.event(eventType)
                            //.data(data)
                            .data(contentString)
                            .build();
                });
    }

    private String extractEventType(String responseString) {
        int startIndex = responseString.indexOf("\"type\":") + "\"type\":".length();
        int endIndex = responseString.indexOf(",", startIndex);
        return responseString.substring(startIndex, endIndex).trim().replace("\"", "");
    }

    private String extractData(String responseString) {
        int startIndex = responseString.indexOf("\"data\":") + "\"data\":".length()+1;
        int endIndex = responseString.lastIndexOf("}");
        return responseString.substring(startIndex, endIndex).trim(); // 설명: responseString에서 startIndex부터 endIndex까지의 문자열을 반환한다.
    }

    private String extractContentString(String data) {
        int startIndex = data.indexOf("\'content\':") + "\'content\':".length();
        int endIndex = data.indexOf("}", startIndex);
        return data.substring(startIndex, endIndex).trim().replace("\"", "");
    }




//    private String extractData(String responseString) {
//        int startIndex = responseString.indexOf("\"data\":") + "\"data\":".length();
//        int endIndex = responseString.lastIndexOf("}");
//        String dataString = responseString.substring(startIndex, endIndex).trim();
//
//        // 문자열을 JSON 형식으로 가공
//        String jsonString = "{\"data\":" + dataString + "}";
//
//        return jsonString;
//    }
}