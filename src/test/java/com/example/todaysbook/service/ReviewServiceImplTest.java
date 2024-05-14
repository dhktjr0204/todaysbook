package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.ReviewRequestDto;
import com.example.todaysbook.domain.entity.Review;
import com.example.todaysbook.repository.ReviewMapper;
import com.example.todaysbook.repository.ReviewRepository;
import com.example.todaysbook.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import reactor.core.publisher.Mono;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceImplTest {

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Mock
    private ReviewMapper reviewMapper;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("리뷰 작성 테스트")
    public void addReviewTest() {

        ReviewRequestDto requestDto = ReviewRequestDto.builder()
                .reviewId(1L)
                .bookId(1L)
                .content("test")
                .score(5)
                .userId(1L)
                .build();

        doReturn(1)
                .when(reviewMapper)
                .addReview(any());

        //when
        int result = reviewService.addReview(requestDto);

        //then
        assertEquals(1, result);

        verify(reviewMapper, times(1)).addReview(any(ReviewRequestDto.class));
    }

    @Test
    @DisplayName("리뷰 삭제 테스트")
    public void deleteReviewTest() {

        // given
        long reviewIdToDelete = 1L;

        // when
        int result = reviewService.deleteReview(reviewIdToDelete);

        // then
        assertEquals(0, result);
        verify(reviewMapper, times(1)).deleteReview(reviewIdToDelete);
    }

    @Test
    @DisplayName("리뷰 수정 테스트")
    public void updateReviewTest() {

        // given
        long reviewId = 1L;
        ReviewRequestDto requestDto = ReviewRequestDto.builder()
                .reviewId(reviewId)
                .bookId(1L)
                .content("updated content")
                .score(4)
                .userId(1L)
                .build();

        doReturn(1)
                .when(reviewMapper)
                .updateReview(any());

        // when
        int result = reviewService.updateReview(requestDto);

        // then
        assertEquals(1, result);

        verify(reviewMapper, times(1)).updateReview(any(ReviewRequestDto.class));
    }

    @Test
    @DisplayName("리뷰 좋아요 추가 테스트")
    public void addLikeReviewTest() {

        //given
        Long userId = 1L;
        Long reviewId = 1L;

        doReturn(1)
                .when(reviewMapper)
                .addLikeReview(userId, reviewId);

        //when
        int result = reviewService.addLikeReview(userId, reviewId);

        //then
        assertEquals(reviewMapper.countLike(reviewId), result);

        verify(reviewMapper, times(1)).addLikeReview(userId, reviewId);
    }

    @Test
    @DisplayName("리뷰 싫어요 추가 테스트")
    public void addDislikeReviewTest() {

        //given
        Long userId = 1L;
        Long reviewId = 1L;

        doReturn(1)
                .when(reviewMapper)
                .addDislikeReview(userId, reviewId);

        //when
        int result = reviewService.addDislikeReview(userId, reviewId);

        //then
        assertEquals(reviewMapper.countDislike(reviewId), result);

        verify(reviewMapper, times(1)).addDislikeReview(userId, reviewId);
    }

    @Test
    @DisplayName("리뷰 좋아요 삭제 테스트")
    public void deleteLikeReviewTest() {

        //given
        Long userId = 1L;
        Long reviewId = 1L;

        doReturn(1)
                .when(reviewMapper)
                .deleteLikeReview(userId, reviewId);

        //when
        int result = reviewService.deleteLikeReview(userId, reviewId);

        //then
        assertEquals(reviewMapper.countLike(reviewId), result);

        verify(reviewMapper, times(1)).deleteLikeReview(userId, reviewId);
    }

    @Test
    @DisplayName("리뷰 싫어요 삭제 테스트")
    public void deleteDislikeReviewTest() {

        //given
        Long userId = 1L;
        Long reviewId = 1L;

        doReturn(1)
                .when(reviewMapper)
                .deleteDislikeReview(userId, reviewId);

        //when
        int result = reviewService.deleteDislikeReview(userId, reviewId);

        //then
        assertEquals(reviewMapper.countDislike(reviewId), result);

        verify(reviewMapper, times(1)).deleteDislikeReview(userId, reviewId);
    }
}
