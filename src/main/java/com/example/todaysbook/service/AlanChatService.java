package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.AlanChatApiRequest;
import com.example.todaysbook.domain.dto.AlanChatApiResponse;
import com.example.todaysbook.domain.dto.AlanChatApiResponseWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlanChatService {

    private static final String API_URL = "https://kdt-api-function.azurewebsites.net/api/v1/question/sse-streaming";

    @Value("${alan.client-id}")
    private String clientId;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AlanChatApiResponse getResponse(AlanChatApiRequest request) {
        String url = API_URL + "?content=" + request.getContent() + "&client_id=" + clientId;

        log.info("alan api에 요청: {}");

        ResponseEntity<StreamingResponseBody> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                StreamingResponseBody.class
        );

        StreamingResponseBody responseBody = responseEntity.getBody();

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            responseBody.writeTo(outputStream);
            String responseString = outputStream.toString("UTF-8");

            Scanner scanner = new Scanner(responseString);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                AlanChatApiResponseWrapper apiResponseWrapper = objectMapper.readValue(line, AlanChatApiResponseWrapper.class);

                if (apiResponseWrapper.getType().equals("complete")) {
                    return new AlanChatApiResponse(apiResponseWrapper.getData().getContent());
                }
            }
        } catch (IOException e) {
            log.error("응답 처리 중 오류 발생: {}", e.getMessage());
        }

        return new AlanChatApiResponse("응답을 받을 수 없습니다.");
    }


}