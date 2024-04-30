package com.example.todaysbook.controller;

import com.example.todaysbook.domain.dto.RecommendListDetailDto;
import com.example.todaysbook.service.RecommendListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController {
    private final RecommendListService recommendListService;

    @GetMapping("/my_recommend_list")
    public String myRecommendList(Model model) {
        long userId = 1;

        List<RecommendListDetailDto> myRecommendListAll = recommendListService.getMyRecommendListAll(userId);

        model.addAttribute("login_user_id", userId);
        model.addAttribute("recommendLists", myRecommendListAll);

        return "user/mypage/my-recommendlist";
    }

    @GetMapping("/my_book_mark_list")
    public String myBookMarkList(Model model){
        long userId=1;

        List<RecommendListDetailDto> myBookMarkListAll = recommendListService.getMyBookMarkListAll(userId);

        model.addAttribute("recommendLists", myBookMarkListAll);

        return "user/mypage/users-recommendlist";
    }
}
