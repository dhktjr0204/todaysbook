package com.example.todaysbook.controller;

import com.example.todaysbook.domain.entity.Orders;
import com.example.todaysbook.repository.OrderRepository;
import com.example.todaysbook.service.CartService;
import com.example.todaysbook.service.GeminiRecommendBookService;
import com.example.todaysbook.domain.dto.BookDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ViewController {

    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final GeminiRecommendBookService geminiRecommendBookService;

    @GetMapping("/admin/gemini-recommend-book")
    public String showGeminiRecommendBooks(Model model) {
        List<BookDto> todayRecommendBooks = geminiRecommendBookService.getTodayRecommendBooks();
        model.addAttribute("todayRecommendBooks", todayRecommendBooks);
        log.info("todayRecommendBooks: " + todayRecommendBooks);
        return "admin/gemini-recommend-book";
    }


    @GetMapping("/alan/chat")
    public String chatbotPage(Model model) {
        return "alan/chat";
    }

}
