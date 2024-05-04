package com.example.todaysbook.controller;

import com.example.todaysbook.domain.dto.CustomUserDetails;
import com.example.todaysbook.domain.dto.FavoriteBookDTO;
import com.example.todaysbook.domain.dto.MyReview;
import com.example.todaysbook.domain.dto.RecommendListDetailDto;
import com.example.todaysbook.service.FavoriteBookService;
import com.example.todaysbook.service.RecommendListService;
import com.example.todaysbook.service.ReviewService;
import com.example.todaysbook.util.Pagination;
import com.example.todaysbook.util.UserChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController {
    private final RecommendListService recommendListService;
    private final ReviewService reviewService;
    private final FavoriteBookService favoriteBookService;

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

    @GetMapping("/review")
    public String myReviewList(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                               @AuthenticationPrincipal CustomUserDetails userDetails,
                               Model model) {

        long userId = UserChecker.getUserId(userDetails);

        Page<MyReview> reviews = reviewService.getMyReviews(userId, pageable);

        int startPage = 0;
        int endPage = 0;

        if (!reviews.isEmpty()) {
            HashMap<String, Integer> pages = Pagination.calculatePage(reviews.getPageable().getPageNumber(), reviews.getTotalPages());
            startPage = pages.get("startPage");
            endPage = pages.get("endPage");
        }

        model.addAttribute("dto", reviews);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "user/mypage/review";
    }

    @GetMapping("/favoritebook")
    public String myFavoriteBooks(@PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable,
                               @AuthenticationPrincipal CustomUserDetails userDetails,
                               Model model) {

        long userId = UserChecker.getUserId(userDetails);

        Page<FavoriteBookDTO> favoriteBooks = favoriteBookService.getFavoriteBooks(userId, pageable);
        int startPage = 0;
        int endPage = 0;

        if (!favoriteBooks.isEmpty()) {
            HashMap<String, Integer> pages = Pagination.calculatePage(favoriteBooks.getPageable().getPageNumber(), favoriteBooks.getTotalPages());
            startPage = pages.get("startPage");
            endPage = pages.get("endPage");
        }

        model.addAttribute("dto", favoriteBooks);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "user/mypage/favorite-book";
    }
}
