package com.example.todaysbook.controller;

import com.example.todaysbook.domain.dto.BookDto;
import com.example.todaysbook.service.GeminiRecommendBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


// MainController에서 TodayRecommendBooks를 사용하기 때문에
// 해당 컨트롤러는 현재 아무곳에서 사용되지 않음. 추후 사용될 수 있음. EX) AJAX 비동기


@RestController
@RequiredArgsConstructor
@RequestMapping("/gemini")
public class GeminiRecommendBooksController {

    private final GeminiRecommendBookService geminiRecommendBookService;

    @GetMapping("/recommendList")
    public List<BookDto> getTodayRecommendBooks() {
        return geminiRecommendBookService.getTodayRecommendBooks();
    }


}