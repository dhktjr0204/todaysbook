package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.Review;
import com.example.todaysbook.domain.dto.ReviewRequestDto;

import java.util.List;

public interface ReviewService {

    List<Review> getReviews(long bookId, long userId);
    int addLikeReview(long userId, long reviewId);
    int addDislikeReview(long userId, long reviewId);
    int deleteLikeReview(long userId, long reviewId);
    int deleteDislikeReview(long userId, long reviewId);
    int addReview(ReviewRequestDto requestDto);
    int deleteReview(long reviewId);
    int updateReview(ReviewRequestDto requestDto);
}
