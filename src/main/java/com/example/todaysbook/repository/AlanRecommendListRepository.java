package com.example.todaysbook.repository;

import com.example.todaysbook.domain.entity.AlanRecommendList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlanRecommendListRepository extends JpaRepository<AlanRecommendList, Long> {

}

