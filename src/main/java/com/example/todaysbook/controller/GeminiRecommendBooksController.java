package com.example.todaysbook.controller;

import com.example.todaysbook.domain.dto.BookDto;
import com.example.todaysbook.domain.entity.Book;
import com.example.todaysbook.service.GeminiRecommendBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gemini")
public class GeminiRecommendBooksController {

    private final GeminiRecommendBookService geminiRecommendBookService;

    @GetMapping("/recommendList")
    public List<BookDto> getTodayRecommendBooks() {
        return geminiRecommendBookService.getTodayRecommendBooks();
    }
}