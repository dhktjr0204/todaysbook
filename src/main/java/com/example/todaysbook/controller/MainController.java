package com.example.todaysbook.controller;

import com.example.todaysbook.domain.dto.CustomUserDetails;
import com.example.todaysbook.domain.dto.RecommendListDetailWithBookMarkDto;
import com.example.todaysbook.service.RecommendListService;
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

    @GetMapping("/")
    public String main(@AuthenticationPrincipal CustomUserDetails userDetails, Model model){

        long userId = 0;

        if(userDetails != null) {
            userId = userDetails.getUserId();
        }

        List<RecommendListDetailWithBookMarkDto> randomUserRecommendList =
                recommendListService.getRandomRecommendList(userId);

        model.addAttribute("userRecommendList", randomUserRecommendList);

        return "index";
    }
}
