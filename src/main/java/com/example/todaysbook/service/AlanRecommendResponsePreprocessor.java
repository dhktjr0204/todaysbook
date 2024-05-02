package com.example.todaysbook.service;

import com.example.todaysbook.exception.InvalidAlanResponseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// Alan API 응답 전처리 전용 클래스

public class AlanRecommendResponsePreprocessor {
    private static final Logger logger = LoggerFactory.getLogger(AlanRecommendResponsePreprocessor.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final int EXPECTED_TITLE_COUNT = 10;

    public static List<String> extractTitles(String response) throws InvalidAlanResponseException {
        try {
            JsonNode responseJson = objectMapper.readTree(response);
            String content = responseJson.get("content").asText();

            List<String> titles = Arrays.stream(content.split("\\n"))
                    .map(line -> line.replaceAll("^\\d+\\.\\s*", "").trim())
                    .collect(Collectors.toList());

            if (titles.stream().anyMatch(String::isEmpty)) {
                throw new InvalidAlanResponseException("Empty title found in the response");
            }

            if (titles.size() != EXPECTED_TITLE_COUNT) {
                throw new InvalidAlanResponseException("Expected " + EXPECTED_TITLE_COUNT + " titles, but found " + titles.size());
            }

            logger.info("책 제목만 추출 완료");
            return titles;
        } catch (JsonProcessingException e) {
            logger.error("api 응답 형식이 예상과 다릅니다.", e);
            throw new InvalidAlanResponseException("API response format is not as expected", e);
        }
    }
}


/*
* TODO: 예외처리 추가하기 (Alan API Retry하기)
*  1. API 응답이 형식이 예상과 다를 때 (계속 추가 예정)
*  2. 책 제목이 비어있을 때 (o)
*  3. 책 제목이 10개가 아닐 때 (o)
* */