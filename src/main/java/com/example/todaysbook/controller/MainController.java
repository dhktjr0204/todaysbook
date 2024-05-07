package com.example.todaysbook.controller;

import com.example.todaysbook.domain.dto.*;
import com.example.todaysbook.service.FavoriteBookService;
import com.example.todaysbook.service.GeminiRecommendBookService;
import com.example.todaysbook.service.RecommendListService;
import com.example.todaysbook.util.UserChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final RecommendListService recommendListService;
    private final FavoriteBookService favoriteBookService;
    private final GeminiRecommendBookService geminiRecommendBookService;

    @GetMapping("/")
    public String main(@AuthenticationPrincipal CustomUserDetails userDetails, Model model){

        long userId = UserChecker.getUserId(userDetails);

        List<FavoriteBookDTO> favoriteBooks =
                favoriteBookService.getFavoriteBooks(userId);

        List<RecommendListDetailWithBookMarkDto> randomUserRecommendList =
                recommendListService.getRandomRecommendList(userId);

        List<BookDto> todayRecommendBooks = geminiRecommendBookService.getTodayRecommendBooks();


        model.addAttribute("favoriteBooks", favoriteBooks);
        model.addAttribute("userRecommendList", randomUserRecommendList);
        model.addAttribute("todayRecommendBooks", todayRecommendBooks);

        return "index";
    }
}
