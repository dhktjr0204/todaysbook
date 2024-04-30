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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class AlanRecommendApiService {

    private final AlanRecommendDataRepository alanRecommendDataRepository;
    private static final Logger logger = LoggerFactory.getLogger(AlanRecommendApiService.class);

    @Scheduled(cron = "0 38 21 * * *", zone = "Asia/Seoul")
    public void scheduleFetchTodaysBooks() {
        fetchTodaysBooks();
    }

    public void fetchTodaysBooks() {

        /*
         * TODO: 예외처리하기 (alan ai API 호출이 실패했을때 또는 응답이 예상치 못한 응답을 했을때)
         *  -> 500응답을 하거나
         *  -> 횟수(n번) 정해서 retry 하기
         * */

        /*
         * TODO: client_id 바꾸는 로직 추가하기 (후순위)
         *  -> client_id당 대략 100번 제한을 넘어가면 다음 client_id로 바꿔서 요청하기
         * */

        String url = "https://kdt-api-function.azurewebsites.net/api/v1/question?content=오늘 기준 한국 도서 베스트셀러 책 제목 15개를 추천해 주세요. 신뢰할 수 있는 최신 정보를 바탕으로 정확한 책 제목을 제공해 주세요. 책 제목만 JSON 형식으로 작성해 주시고, 구어체는 사용하지 마세요. 추가적인 설명이나 마크다운은 포함하지 않도록 해 주세요.&client_id=7348d628-9c3b-455e-bc1b-94ed9cc05b63";

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        logger.info("API Response: {}", response);

        // API 응답 파싱
        ObjectMapper responseMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = responseMapper.readTree(response);
        } catch (Exception e) {
            logger.error("API response 파싱 실패", e);
            throw new RuntimeException("API response 파싱 실패", e);
        }

        // content 분리 (여러가지 형식의 응답을 처리하도록 여러 조건을 추가하기)
        String content;
        if (jsonNode.has("content")) {
            content = jsonNode.get("content").asText();
        } else if (jsonNode.has("action") && jsonNode.get("action").has("name") && jsonNode.get("action").get("name").asText().equals("search_web")) {
            String searchWebContent = jsonNode.get("content").asText();
            int startIndex = searchWebContent.indexOf("```");
            int endIndex = searchWebContent.lastIndexOf("```");
            if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
                content = searchWebContent.substring(startIndex + 3, endIndex).trim();
            } else {
                logger.error("유효하지 않은 search_web content 형식");
                throw new RuntimeException("유효하지 않은 search_web content 형식");
            }
        } else {
            logger.error("유효하지 않은 API 응답 형식");
            throw new RuntimeException("유효하지 않은 API 응답 형식");
        }

// content를 JSON 배열로 파싱
        ObjectMapper contentMapper = new ObjectMapper();
        JsonNode contentNode;
        try {
            // 실제 JSON 데이터 부분 추출
            int jsonStartIndex = content.indexOf("[");
            int jsonEndIndex = content.lastIndexOf("]");
            if (jsonStartIndex != -1 && jsonEndIndex != -1 && jsonEndIndex > jsonStartIndex) {
                String jsonContent = content.substring(jsonStartIndex, jsonEndIndex + 1);
                contentNode = contentMapper.readTree(jsonContent);
            } else {
                logger.error("유효하지 않은 JSON 데이터 형식");
                throw new RuntimeException("유효하지 않은 JSON 데이터 형식");
            }
        } catch (JsonProcessingException e) {
            logger.error("content 파싱 실패", e);
            throw new RuntimeException("content 파싱 실패", e);
        }


        // JSON 배열에서 책 제목 추출
        List<AlanRecommendDataDto> alanRecommendDataDtos = StreamSupport.stream(contentNode.spliterator(), false)
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
        //추출한 책 제목 모두 출력
        logger.info("추출한 책 제목: {}", alanRecommendDataDtos.stream().map(AlanRecommendDataDto::getTitle).collect(Collectors.toList()));
        logger.info("책 제목만 추출 완료");

// 추출한 책 제목을 AlanRecommendData 엔티티에 저장
        List<AlanRecommendData> alanRecommendDataList = alanRecommendDataDtos.stream()
                .map(dto -> AlanRecommendData.builder()
                        .title(dto.getTitle())
                        .createdAt(dto.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        alanRecommendDataRepository.saveAll(alanRecommendDataList);
        logger.info("AlanRecommendData 엔티티에 저장 완료");
    }


    /*
    * TODO: 프롬프트 수정하기
    *  client_id가 달라도 서로 영향을 받는다.
    *  영향을 받아도 최대한 원하는 데이터 받을수 있도록 하기
    *  ex) 프롬프트: 전 메시지는 완전히 잊고 현재 질문만 답변해주세요.
    * */

    /*
    * TODO: 프롬프트 수정하기 (정확도)
    *  고민해야하는점: 한두개가 정확하지 않다.
    *  ex) "제목": "이달의 책 2024년 4월", (검색해보니 이런 책은 없다. 교보문고 이달의 책 웹페이지임.)
    * */

    /*
     * TODO: 프롬프트 수정하기 (중복)
     *  답변에서 추천하는 책이 날짜가 바뀌어도 같은 책이 추천되는 경우가 있다.
     *  그 경우가 심하면 조정을 해야할 지 고민하기
     * */

    /*
    * TODO: 프롬프트 수정하기 (날짜 자동 없데이트)
    *  프롬프트에서 날짜기준으로 물어보는데 그 날짜를 자동으로 어제 기준으로 업데이트되어서 물어보도록 수정하기
    * */


}
