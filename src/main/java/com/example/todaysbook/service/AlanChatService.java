package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.AlanChatApiRequest;
import com.example.todaysbook.domain.dto.AlanChatApiResponse;
import com.example.todaysbook.domain.dto.AlanChatApiResponseWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AlanChatService {

    private static final String API_URL = "https://kdt-api-function.azurewebsites.net/api/v1/question/sse-streaming";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AlanChatApiResponse getResponse(AlanChatApiRequest request) {
        // API 호출
        ResponseEntity<String> response = restTemplate.postForEntity(API_URL, request, String.class);

        // 응답 처리 로직 구현
        String responseBody = response.getBody();
        AlanChatApiResponse apiResponse = new AlanChatApiResponse();

        try {
            // JSON 문자열을 파싱하여 객체로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            AlanChatApiResponseWrapper responseWrapper = objectMapper.readValue(responseBody, AlanChatApiResponseWrapper.class);

            // 응답 데이터 추출
            String content = responseWrapper.getData().getContent();

            // AlanChatApiResponse 객체에 응답 내용 설정
            apiResponse.setContent(content);

        } catch (JsonProcessingException e) {
            // JSON 파싱 실패 시 예외 처리
            e.printStackTrace();
            // 예외 처리에 따른 적절한 응답 설정
            apiResponse.setContent("죄송합니다. 응답을 처리하는 중에 오류가 발생했습니다.");
        }

        return apiResponse;
    }
}