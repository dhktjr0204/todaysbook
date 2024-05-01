package com.example.todaysbook.repository;

import com.example.todaysbook.domain.entity.AlanRecommendData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlanRecommendDataRepository extends JpaRepository<AlanRecommendData, Long> {
    List<AlanRecommendData> findByCreatedAtBetween(LocalDateTime localDateTime, LocalDateTime localDateTime1);
}
