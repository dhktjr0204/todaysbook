package com.example.todaysbook.controller;

import com.example.todaysbook.domain.dto.AlanRecommendListDto;
import com.example.todaysbook.service.AlanRecommendListService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class AlanRecommendListController {

    private final AlanRecommendListService alanRecommendListService;

    // TODO: 오늘의 책 리스트 조회 API
}