package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.MyReview;
import com.example.todaysbook.domain.dto.Review;
import com.example.todaysbook.domain.dto.ReviewRequestDto;
import com.example.todaysbook.domain.dto.SimpleReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewService {

    List<Review> getReviews(long bookId, long userId, String orderBy);
    List<SimpleReview> getSimpleReviews();
    Page<MyReview> getMyReviews(long userId, Pageable pageable);
    int addLikeReview(long userId, long reviewId);
    int addDislikeReview(long userId, long reviewId);
    int deleteLikeReview(long userId, long reviewId);
    int deleteDislikeReview(long userId, long reviewId);
    int addReview(ReviewRequestDto requestDto);
    int deleteReview(long reviewId);
    int updateReview(ReviewRequestDto requestDto);
}
