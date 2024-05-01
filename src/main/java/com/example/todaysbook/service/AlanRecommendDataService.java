package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.AlanRecommendDataDto;
import com.example.todaysbook.domain.entity.AlanRecommendData;
import com.example.todaysbook.repository.AlanRecommendDataRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlanRecommendDataService {

    private final AlanRecommendDataRepository alanRecommendDataRepository;
    private final AlanRecommendApiService alanRecommendApiService;

    private static final Logger logger = LoggerFactory.getLogger(AlanRecommendApiService.class);

    public void saveTodaysBooks() {
        List<AlanRecommendDataDto> alanRecommendDataDtos = alanRecommendApiService.fetchTodaysBooks();

        List<AlanRecommendData> alanRecommendDataList = alanRecommendDataDtos.stream()
                .map(dto -> AlanRecommendData.builder()
                        .title(dto.getTitle())
                        .createdAt(dto.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        alanRecommendDataRepository.saveAll(alanRecommendDataList);
        logger.info("\nAlanRecommendData 엔티티에 저장 완료");
    }
}