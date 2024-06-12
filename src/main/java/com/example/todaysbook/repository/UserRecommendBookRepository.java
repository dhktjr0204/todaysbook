package com.example.todaysbook.repository;

import com.example.todaysbook.domain.entity.UserRecommendBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRecommendBookRepository extends JpaRepository<UserRecommendBook,Long> {
    Optional<List<UserRecommendBook>> findByUserRecommendListIdOrderByOrder(Long id);
    Long deleteByBookIdAndUserRecommendListId(Long bookId, Long listId);
}
