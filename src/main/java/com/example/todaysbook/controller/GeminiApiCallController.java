package com.example.todaysbook.controller;

import com.example.todaysbook.service.GeminiRecommendBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gemini")
public class GeminiApiCallController {

    private final GeminiRecommendBookService geminiRecommendBookService;

//    @Scheduled(cron = "${scheduler.cron.expression}")
//    @GetMapping("/ApiCall")
//    public ResponseEntity<String> ScheduledGeminiApiCall() {
//        return geminiRecommendBookService.callScheduledGeminiApi();
//    }
//
//    @PostMapping("/recommendBooks")
//    public ResponseEntity<?> recommendBooks(@RequestParam("quantity") int quantity,
//                                            @RequestParam("temperature") double temperature) {
//        try {
//            geminiRecommendBookService.recommendAndSaveBooks(quantity, temperature);
//            return ResponseEntity.ok().build();
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//        }
//    }

}