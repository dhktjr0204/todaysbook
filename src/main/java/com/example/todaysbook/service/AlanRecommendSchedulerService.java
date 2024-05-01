package com.example.todaysbook.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlanRecommendSchedulerService {

    private final AlanRecommendDataService alanRecommendDataService;
    private static final Logger logger = LoggerFactory.getLogger(AlanRecommendApiService.class);

    @Scheduled(cron = "0 24 09 * * *", zone = "Asia/Seoul")
    public void scheduleFetchTodaysBooks() {
        logger.info("\nAlan 오늘의책 스케줄러 실행");
        alanRecommendDataService.saveTodaysBooks();

    }
}