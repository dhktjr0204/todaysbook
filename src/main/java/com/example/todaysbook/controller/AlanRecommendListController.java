package com.example.todaysbook.controller;

import com.example.todaysbook.service.AlanRecommendListService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class AlanRecommendListController {

    private final AlanRecommendListService alanRecommendListService;

    // TODO: 오늘의 ai 추천 책 리스트 조회 API


}