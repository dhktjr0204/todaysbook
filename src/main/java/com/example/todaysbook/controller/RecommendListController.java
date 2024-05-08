package com.example.todaysbook.controller;

import com.example.todaysbook.domain.dto.CustomUserDetails;
import com.example.todaysbook.domain.dto.RecommendListCreateRequestDto;
import com.example.todaysbook.domain.dto.RecommendListUpdateRequestDto;
import com.example.todaysbook.domain.dto.RecommendListDetailDto;
import com.example.todaysbook.domain.entity.UserRecommendList;
import com.example.todaysbook.exception.user.UserValidateException;
import com.example.todaysbook.service.RecommendListService;
import com.example.todaysbook.validate.RecommendListCreateValidator;
import com.example.todaysbook.validate.RecommendListUpdateValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String getWriteForm(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

        if(userDetails==null){
            return "error/404";
        }

        model.addAttribute("recommendList", new RecommendListCreateRequestDto());

        return "user/mypage/create-recommendlist";
    }

    @PostMapping("/add")
    public ResponseEntity<String> createRecommnedList(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                      RecommendListCreateRequestDto request,
                                                      BindingResult result) {

        if(userDetails==null){
            throw new UserValidateException();
        }

        Long userId = userDetails.getUserId();

        //title 길이, list 길이 검증
        RecommendListCreateValidator listValidator=new RecommendListCreateValidator();
        listValidator.validate(request, result);

        UserRecommendList save = recommendListService.save(userId, request);

        return ResponseEntity.ok("/mypage/my_recommend_list");
    }

    @GetMapping("/update/{id}")
    public String getUpdateForm(@AuthenticationPrincipal CustomUserDetails userDetails,
                                @PathVariable Long id, Model model) {

        RecommendListDetailDto recommendListDetail = recommendListService.getRecommendListDetail(id);

        //리스트 작성 유저와 현재 로그인 된 유저가 다를 시 404페이지 뜸
        if(userDetails==null || userDetails.getUserId() != recommendListDetail.getUserId()){
            return "error/404";
        }

        model.addAttribute("recommendList", recommendListDetail);

        return "user/mypage/update-recommendlist";
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateRecommendList(@PathVariable Long id,
                                                      RecommendListUpdateRequestDto request,
                                                      BindingResult result) {

        //title 길이, list 길이 검증
        RecommendListUpdateValidator listValidator=new RecommendListUpdateValidator();
        listValidator.validate(request, result);

        recommendListService.update(id, request);

        return ResponseEntity.ok("/mypage/my_recommend_list");
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> deleteRecommendList(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                      @PathVariable(value = "id") Long listId,
                                                      Long userId) {

        //현재 로그인한 유저랑 리스트 만든 유저랑 다를때 예외 처리
        if(userDetails.getUserId()!=userId){
            throw new UserValidateException();
        }

        recommendListService.delete(listId);

        return ResponseEntity.ok("/mypage/my_recommend_list");
    }

}