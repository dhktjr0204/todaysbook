package com.example.todaysbook.service;

public interface ReviewService {

    int addLikeReview(long userId, long reviewId);
    int addDislikeReview(long userId, long reviewId);
    int deleteLikeReview(long userId, long reviewId);
    int deleteDislikeReview(long userId, long reviewId);
}
