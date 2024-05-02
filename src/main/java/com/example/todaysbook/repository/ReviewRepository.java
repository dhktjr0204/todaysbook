package com.example.todaysbook.repository;

import com.example.todaysbook.domain.dto.MyReview;
import com.example.todaysbook.domain.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query(value = "select new com.example.todaysbook.domain.dto.MyReview( " +
            "r.id, r.bookId, r.userId, b.title, r.content) " +
            "from Review r " +
            "inner join Book b " +
            "on b.id = r.bookId " +
            "where r.userId = :userId")
    Page<MyReview> findReviewsByUserId(Long userId, Pageable pageable);
}
