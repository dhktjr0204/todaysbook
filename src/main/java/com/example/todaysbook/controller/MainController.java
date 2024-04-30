package com.example.todaysbook.controller;

import com.example.todaysbook.domain.dto.RandomRecommendListDetailDto;
import com.example.todaysbook.service.RecommendListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final RecommendListService recommendListService;

    @GetMapping("/")
    public String main(Model model){
        long userId=1;

        List<RandomRecommendListDetailDto> randomUserRecommendList =
                recommendListService.getRandomRecommendList(userId);

        model.addAttribute("userRecommendList", randomUserRecommendList);

        return "index";
    }
}
