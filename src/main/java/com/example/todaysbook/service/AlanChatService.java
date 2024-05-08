package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.AlanChatApiRequest;
import com.example.todaysbook.domain.dto.AlanChatApiResponse;
import com.example.todaysbook.domain.dto.AlanChatApiResponseWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class AlanChatService {

    private static final String API_URL = "https://kdt-api-function.azurewebsites.net/api/v1/question/sse-streaming";

    @Value("${alan.client-id}")
    private String clientId;

    private final RestTemplate restTemplate;

    public AlanChatApiResponse getResponse(AlanChatApiRequest request) {
        String url = API_URL + "?content=" + request.getContent() + "&client_id=" + clientId;

        ResponseExtractor<AlanChatApiResponse> responseExtractor = response -> {
            Scanner scanner = new Scanner(response.getBody(), "UTF-8");
            StringBuilder contentBuilder = new StringBuilder();

            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (line.startsWith("data:")) {
                    String data = line.substring(5).trim();
                    if (!data.isEmpty()) {
                        try {
                            // 작은따옴표를 큰따옴표로 변경
                            data = data.replace("'", "\"");
                            AlanChatApiResponseWrapper responseWrapper = new ObjectMapper().readValue(data, AlanChatApiResponseWrapper.class);
                            contentBuilder.append(responseWrapper.getData().getContent());
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException("Failed to parse response data", e);
                        }
                    }
                }
            }

            return AlanChatApiResponse.builder()
                    .content(contentBuilder.toString())
                    .build();
        };

        AlanChatApiResponse apiResponse = restTemplate.execute(url, HttpMethod.GET, null, responseExtractor);

        return apiResponse;
    }
}