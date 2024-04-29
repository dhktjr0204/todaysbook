package com.example.todaysbook.repository;

import com.example.todaysbook.domain.entity.UserRecommendList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRecommendListRepository extends JpaRepository<UserRecommendList,Long> {
}
