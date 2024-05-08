package com.example.todaysbook.controller;

import com.example.todaysbook.domain.dto.BookDetailDto;
import com.example.todaysbook.domain.dto.CustomUserDetails;
import com.example.todaysbook.domain.dto.RecommendBookDto;
import com.example.todaysbook.domain.dto.ReviewRequestDto;
import com.example.todaysbook.service.BookDetailService;
import com.example.todaysbook.service.RecommendBookService;
import com.example.todaysbook.util.UserChecker;
import lombok.RequiredArgsConstructor;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookDetailController {

    private final BookDetailService bookDetailService;
    private final RecommendBookService recommendBookService;

    @RequestMapping("/book/detail/{bookId}")
    public String getBookDetail(@PathVariable Long bookId, Model model,
                                @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException, TasteException {

        long userId = UserChecker.getUserId(userDetails);

        BookDetailDto bookDetailDto = bookDetailService.getBookDetail(bookId, userId);
        //recommendBookService.GenerateRecommendBookList();
        List<RecommendBookDto> recommendedBooks = recommendBookService.getRecommendBooks(bookId);

        model.addAttribute("bookDetailDto", bookDetailDto);
        model.addAttribute("recommendedBooks", recommendedBooks);
        model.addAttribute("review", new ReviewRequestDto());
        model.addAttribute("userId", userId);

        return "book/detail";
    }
}