package com.example.todaysbook.service;

import com.example.todaysbook.exception.InvalidAlanResponseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class AlanRecommendResponsePreprocessor {
    private static final Logger logger = LoggerFactory.getLogger(AlanRecommendResponsePreprocessor.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final int EXPECTED_TITLE_COUNT = 10;

    public static List<String> extractTitles(String response) throws InvalidAlanResponseException {
        List<String> titles = new ArrayList<>();

        try {
            JsonNode responseJson = objectMapper.readTree(response);
            String content = responseJson.get("content").asText();

            // 개행 문자를 기준으로 문자열 분리
            String[] lines = content.split("\\n");

            for (String line : lines) {
                // 각 줄에서 숫자와 마침표를 제거하고 제목만 추출
                String title = line.replaceAll("^\\d+\\.\\s*", "").trim();

                // 비어있는 책 제목이 있는지 검사
                if (title.isEmpty()) {
                    throw new InvalidAlanResponseException("Empty title found in the response");
                }

                titles.add(title);
            }

            // 책 제목이 총 10개인지 확인
            if (titles.size() != EXPECTED_TITLE_COUNT) {
                throw new InvalidAlanResponseException("Expected " + EXPECTED_TITLE_COUNT + " titles, but found " + titles.size());
            }
        } catch (JsonProcessingException e) {
            logger.error("api 응답 형식이 예상과 다릅니다.", e);
            throw new InvalidAlanResponseException("API response format is not as expected", e);
        }

        logger.info("\n책 제목만 추출 완료");
        return titles;
    }
}



/*
* TODO: 예외처리 추가하기 (Alan API Retry하기)
*  1. API 응답이 형식이 예상과 다를 때 (계속 추가 예정)
*  2. 책 제목이 비어있을 때 (o)
*  3. 책 제목이 10개가 아닐 때 (o)
* */