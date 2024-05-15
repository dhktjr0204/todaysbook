package com.example.todaysbook.repository;

import com.example.todaysbook.domain.dto.MyReview;
import com.example.todaysbook.domain.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query(value = "select new com.example.todaysbook.domain.dto.MyReview( " +
            "r.id, r.bookId, r.userId, b.title, r.content, r.score) " +
            "from Review r " +
            "inner join Book b " +
            "on b.id = r.bookId " +
            "where r.userId = :userId")
    Page<MyReview> findReviewsByUserId(Long userId, Pageable pageable);

    @Query(value = "select new com.example.todaysbook.domain.dto.MyReview( " +
            "r.id, r.bookId, r.userId, b.title, r.content, r.score) " +
            "from Review r " +
            "inner join Book b " +
            "on b.id = r.bookId " +
            "where r.userId = :userId")
    List<MyReview> findReviewsByUserId(Long userId);

    @Query(value = "select user_id from review where id = :reviewId", nativeQuery = true)
    Long findReviewOwnerId(Long reviewId);
}
