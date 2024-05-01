package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.AlanRecommendDataDto;
import com.example.todaysbook.domain.entity.AlanRecommendData;
import com.example.todaysbook.repository.AlanRecommendDataRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class AlanRecommendDataService {

    private final AlanRecommendDataRepository alanRecommendDataRepository;
    private final AlanRecommendApiService alanRecommendApiService;

    private static final Logger logger = LoggerFactory.getLogger(AlanRecommendDataService.class);

    public void saveTodaysBooks() {
        String response = alanRecommendApiService.callApi();
        logger.info("\nAPI 호출 완료");
        String content = parseApiResponse(response);
        logger.info("\nAPI 응답 파싱 완료");
        List<AlanRecommendDataDto> alanRecommendDataDtos = extractBookTitles(content);
        logger.info("\n책 제목만 추출 완료");
        logger.info("\n추출한 책 제목: {}", alanRecommendDataDtos.stream().map(AlanRecommendDataDto::getTitle).collect(Collectors.toList()));

        List<AlanRecommendData> alanRecommendDataList = alanRecommendDataDtos.stream()
                .map(dto -> AlanRecommendData.builder()
                        .title(dto.getTitle())
                        .createdAt(dto.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        //alanRecommendDataRepository.saveAll(alanRecommendDataList);
        //logger.info("\nAlanRecommendData 엔티티에 저장 완료");

        //saveAlanRecommendBook호출

    }

    // API 응답 파싱 메서드
    private String parseApiResponse(String response) {
        ObjectMapper responseMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = responseMapper.readTree(response);
        } catch (Exception e) {
            logger.error("\nAPI response 파싱 실패", e);
            throw new RuntimeException("API response 파싱 실패", e);
        }

        if (jsonNode.has("content")) {
            return jsonNode.get("content").asText();
        } else if (jsonNode.has("action") && jsonNode.get("action").has("name") && jsonNode.get("action").get("name").asText().equals("search_web")) {
            String searchWebContent = jsonNode.get("content").asText();
            int startIndex = searchWebContent.indexOf("```");
            int endIndex = searchWebContent.lastIndexOf("```");
            if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
                return searchWebContent.substring(startIndex + 3, endIndex).trim();
            } else {
                logger.error("\n유효하지 않은 search_web content 형식");
                throw new RuntimeException("유효하지 않은 search_web content 형식");
            }
        } else {
            logger.error("\n유효하지 않은 API 응답 형식");
            throw new RuntimeException("유효하지 않은 API 응답 형식");
        }
    }

    // 제목 추출 메서드
    private List<AlanRecommendDataDto> extractBookTitles(String content) {
        ObjectMapper contentMapper = new ObjectMapper();
        JsonNode contentNode;
        try {
            if (content.startsWith("{")) {
                contentNode = contentMapper.readTree(content);
                return StreamSupport.stream(contentNode.spliterator(), false)
                        .map(node -> AlanRecommendDataDto.builder()
                                .title(node.asText().replaceFirst("^\\d+\\.\\s*", ""))
                                .createdAt(LocalDateTime.now())
                                .build())
                        .toList();
            } else {
                int jsonStartIndex = content.indexOf("[");
                int jsonEndIndex = content.lastIndexOf("]");
                if (jsonStartIndex != -1 && jsonEndIndex != -1 && jsonEndIndex > jsonStartIndex) {
                    String jsonContent = content.substring(jsonStartIndex, jsonEndIndex + 1);
                    contentNode = contentMapper.readTree(jsonContent);
                } else {
                    logger.error("\n유효하지 않은 JSON 데이터 형식");
                    throw new RuntimeException("유효하지 않은 JSON 데이터 형식");
                }
            }
        } catch (JsonProcessingException e) {
            logger.error("\ncontent 파싱 실패", e);
            throw new RuntimeException("content 파싱 실패", e);
        }

        return StreamSupport.stream(contentNode.spliterator(), false)
                .map(node -> {
                    String title = node.get("content").asText()
                            .replaceFirst("^\\d+\\.\\s*", "")
                            .replaceAll("^'|'$", "");
                    return AlanRecommendDataDto.builder()
                            .title(title)
                            .createdAt(LocalDateTime.now())
                            .build();
                })
                .toList();
    }
}