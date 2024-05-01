package com.example.todaysbook.controller;

import com.example.todaysbook.domain.dto.Review;
import com.example.todaysbook.domain.dto.ReviewRequestDto;
import com.example.todaysbook.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/add")
    public String addNewReview(@RequestBody ReviewRequestDto requestDto,
                               Model model) {

        long userId = 1l;

        requestDto.setUserId(userId);
        int flag = reviewService.addReview(requestDto);

        if(errorHandler(flag)) {

            throw new IllegalStateException("ddd");
        }

        List<Review> reviews = reviewService.getReviews(requestDto.getBookId(), requestDto.getUserId());
        model.addAttribute("reviews", reviews);

        return "book/review";
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteReview(@RequestParam(value = "bookId") long bookId) {

        try {

            long userId = 1l;

            return ResponseEntity.ok(reviewService.deleteReview(userId, bookId));

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/add_like")
    public ResponseEntity<?> addLikeReview(@RequestParam(value = "userId") long userId,
                                           @RequestParam(value = "reviewId") long reviewId) {

        try {

            return ResponseEntity.ok(reviewService.addLikeReview(userId, reviewId));

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/add_dislike")
    public ResponseEntity<?> addDislikeReview(@RequestParam(value = "userId") long userId,
                                           @RequestParam(value = "reviewId") long reviewId) {

        try {

            return ResponseEntity.ok(reviewService.addDislikeReview(userId, reviewId));

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete_like")
    public ResponseEntity<?> deleteLikeReview(@RequestParam(value = "userId") long userId,
                                              @RequestParam(value = "reviewId") long reviewId) {

        try {

            return ResponseEntity.ok(reviewService.deleteLikeReview(userId, reviewId));

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete_dislike")
    public ResponseEntity<?> deleteDislikeReview(@RequestParam(value = "userId") long userId,
                                              @RequestParam(value = "reviewId") long reviewId) {

        try {

            return ResponseEntity.ok(reviewService.deleteDislikeReview(userId, reviewId));

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    private boolean errorHandler(int flag) {

        return flag < 1;
    }
}
