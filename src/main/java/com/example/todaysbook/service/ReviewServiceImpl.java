package com.example.todaysbook.service;

import com.example.todaysbook.repository.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewMapper reviewMapper;

    @Override
    public int addLikeReview(long userId, long reviewId) {

        reviewMapper.addLikeReview(userId, reviewId);

        return reviewMapper.countLike(reviewId);
    }

    @Override
    public int addDislikeReview(long userId, long reviewId) {

        reviewMapper.addDislikeReview(userId, reviewId);

        return reviewMapper.countDislike(reviewId);
    }

    @Override
    public int deleteLikeReview(long userId, long reviewId) {

        reviewMapper.deleteLikeReview(userId, reviewId);

        return reviewMapper.countLike(reviewId);
    }

    @Override
    public int deleteDislikeReview(long userId, long reviewId) {

        reviewMapper.deleteDislikeReview(userId, reviewId);

        return reviewMapper.countDislike(reviewId);
    }
}
