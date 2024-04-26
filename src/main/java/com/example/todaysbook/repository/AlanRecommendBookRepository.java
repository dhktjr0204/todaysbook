package com.example.todaysbook.repository;

import com.example.todaysbook.domain.entity.AlanRecommendBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlanRecommendBookRepository extends JpaRepository<AlanRecommendBook, Long> {
}
