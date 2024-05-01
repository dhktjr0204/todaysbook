package com.example.todaysbook.scheduling;

import com.example.todaysbook.service.AlanRecommendBookService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlanRecommendScheduler {

    private final AlanRecommendBookService alanRecommendBookService;
    private static final Logger logger = LoggerFactory.getLogger(AlanRecommendScheduler.class);

    @Scheduled(cron = "${scheduler.cron.expression}", zone = "Asia/Seoul")
    public void scheduleFetchTodaysBooks() {
        logger.info("Alan 오늘의책 스케줄러 실행");
        alanRecommendBookService.saveAlanRecommendBooks();
    }
}
