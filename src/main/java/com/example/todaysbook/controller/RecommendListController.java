package com.example.todaysbook.controller;

import com.example.todaysbook.domain.dto.RecommendListCreateRequestDto;
import com.example.todaysbook.domain.dto.RecommendListUpdateRequestDto;
import com.example.todaysbook.domain.dto.RecommendListDetailDto;
import com.example.todaysbook.domain.entity.UserRecommendList;
import com.example.todaysbook.service.RecommendListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recommend")
public class RecommendListController {

    private final RecommendListService recommendListService;

    @GetMapping("/detail/{id}")
    public String getRecommendListDetail(@PathVariable Long id, Model model) {
        RecommendListDetailDto result = recommendListService.getRecommendListDetail(id);

        model.addAttribute("recommendList", result);

        return "recommendList/recommend-list-detail";
    }

    @GetMapping("/add")
    public String getWriteForm(Model model) {
        long loginUserId=1;

        model.addAttribute("login_user_id", loginUserId);
        model.addAttribute("recommendList", new RecommendListCreateRequestDto());

        return "user/mypage/create-recommendlist";
    }

    @PostMapping("/add")
    public ResponseEntity<String> createRecommnedList(RecommendListCreateRequestDto request) {
        long userId = 1;

        UserRecommendList save = recommendListService.save(userId, request);

        return ResponseEntity.ok("/mypage/my_recommend_list/"+save.getUserId());
    }

    @GetMapping("/update/{id}")
    public String getUpdateForm(@PathVariable Long id, Model model) {
        long loginUserId=1;

        RecommendListDetailDto recommendListDetail = recommendListService.getRecommendListDetail(id);

        model.addAttribute("login_user_id", loginUserId);
        model.addAttribute("recommendList", recommendListDetail);

        return "user/mypage/update-recommendlist";
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateRecommendList(@PathVariable Long id, RecommendListUpdateRequestDto request) {

        recommendListService.update(id, request);

        return ResponseEntity.ok("/mypage/my_recommend_list/"+request.getUserId());
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> deleteRecommendList(@PathVariable(value = "id") Long listId, Long loginUserId) {

        recommendListService.delete(listId);

        return ResponseEntity.ok("/mypage/my_recommend_list/"+loginUserId);
    }

}