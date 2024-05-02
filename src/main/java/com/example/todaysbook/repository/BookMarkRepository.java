package com.example.todaysbook.repository;

import com.example.todaysbook.domain.entity.BookMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookMarkRepository extends JpaRepository<BookMark,Long> {
    Optional<BookMark> findByUserIdAndUserRecommendListId(Long userId, Long listId);
}
