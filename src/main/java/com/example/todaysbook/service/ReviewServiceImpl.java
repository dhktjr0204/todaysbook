package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.MyReview;
import com.example.todaysbook.domain.dto.Review;
import com.example.todaysbook.domain.dto.ReviewRequestDto;
import com.example.todaysbook.domain.dto.SimpleReview;
import com.example.todaysbook.repository.ReviewMapper;
import com.example.todaysbook.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewMapper reviewMapper;
    private final ReviewRepository reviewRepository;
    @Override
    public List<Review> getReviews(long bookId, long userId, String orderBy) {

        return reviewMapper.getReviews(bookId, userId, orderBy);
    }

    @Override
    public List<SimpleReview> getSimpleReviews() {

        return reviewMapper.getSimpleReviews();
    }

    @Override
    public Page<MyReview> getMyReviews(long userId, Pageable pageable) {

        return reviewRepository.findReviewsByUserId(userId, pageable);
    }

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

    @Override
    public int addReview(ReviewRequestDto requestDto) {

        return reviewMapper.addReview(requestDto);
    }

    @Override
    public int deleteReview(long reviewId) {

        return reviewMapper.deleteReview(reviewId);
    }

    @Override
    public int updateReview(ReviewRequestDto requestDto) {

        return reviewMapper.updateReview(requestDto);
    }
}
