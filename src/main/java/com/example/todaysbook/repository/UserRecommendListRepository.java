package com.example.todaysbook.repository;

import com.example.todaysbook.domain.dto.RecommendListDto;
import com.example.todaysbook.domain.entity.UserRecommendList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRecommendListRepository extends JpaRepository<UserRecommendList,Long> {
    @Query(value = "select new com.example.todaysbook.domain.dto.RecommendListDto(url.id, url.title, url.userId, u.nickName, url.date) " +
            "from UserRecommendList url " +
            "inner join User u " +
            "on u.id=url.userId " +
            "where url.title like %:keyword%")
    Page<RecommendListDto> findUserRecommendListByKeyword(String keyword, Pageable pageable);
}
