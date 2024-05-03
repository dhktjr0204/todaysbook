package com.example.todaysbook.controller;

import com.example.todaysbook.service.GeminiRecommendBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gemini")
public class GeminiApiCallController {

    private final GeminiRecommendBookService geminiRecommendBookService;

    @GetMapping("/question")
    public ResponseEntity<?> GeminiApiCall() {
        try {
            return ResponseEntity.ok().body(geminiRecommendBookService.getContents("오늘 한국 기준으로 최근에 많이 팔린 책의 제목 10개를 추천해 주세요. 신뢰할 수 있는 최신 정보를 바탕으로 정확한 책 제목만 나열하여 주세요. 존재하지 않는 책 제목은 추천하지 마세요. 답변에는 책의 저자, 출처, 참고, 이미지 등 다른 내용은 포함하지 말아주세요."));
        } catch (HttpClientErrorException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}