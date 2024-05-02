package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.AlanRecommendDataDto;
import com.example.todaysbook.domain.entity.AlanRecommendData;
import com.example.todaysbook.exception.InvalidAlanResponseException;
import com.example.todaysbook.repository.AlanRecommendDataRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class AlanRecommendDataService {

    private final AlanRecommendApiService alanRecommendApiService;
    private final AlanRecommendDataRepository alanRecommendDataRepository;
    private static final Logger logger = LoggerFactory.getLogger(AlanRecommendDataService.class);

    // 오늘 날짜에 해당하는 AlanRecommendData 엔티티를 조회하는 메서드
    public List<AlanRecommendDataDto> getAlanRecommendDataByToday() {
        saveAlanRecommendPreprocessedData();
        return alanRecommendDataRepository.findByCreatedAt(LocalDateTime.now()).stream()
                .map(AlanRecommendData::toDto)
                .collect(Collectors.toList());
    }


    // AlanRecommendResponsePreprocessor를 통해서 전처리된 데이터를 AlanRecommendData엔티티에 저장하는 메소드
    public void saveAlanRecommendPreprocessedData() {
        // Alan API 호출
        String response = alanRecommendApiService.callApi();

        List<String> titles;
        try {
            // 책 제목 추출 메서드 호출
            titles = AlanRecommendResponsePreprocessor.extractTitles(response);
        } catch (InvalidAlanResponseException e) {
            logger.error("Invalid API response", e);
            throw new RuntimeException("책 제목 추출 실패", e);
        }

        // 책 제목 하나씩 AlanRecommendData 엔티티에 저장
        List<AlanRecommendData> alanRecommendDataList = titles.stream()
                .map(title -> AlanRecommendData.builder().title(title).createdAt(LocalDateTime.now()).build())
                .collect(Collectors.toList());
        alanRecommendDataRepository.saveAll(alanRecommendDataList);
        logger.info("AlanRecommendData 엔티티에 저장 완료");
    }


}
