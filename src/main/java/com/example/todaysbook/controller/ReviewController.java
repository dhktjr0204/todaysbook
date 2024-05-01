package com.example.todaysbook.controller;

import com.example.todaysbook.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

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
}
