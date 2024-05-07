package com.example.todaysbook.repository;

import com.example.todaysbook.domain.dto.RecommendListDto;
import com.example.todaysbook.domain.dto.RecommendListWithBookMarkDto;
import com.example.todaysbook.domain.entity.UserRecommendList;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRecommendListRepository extends JpaRepository<UserRecommendList,Long> {
    @Query(value = "select new com.example.todaysbook.domain.dto.RecommendListWithBookMarkDto(url.id, url.title, url.userId, u.nickName, url.date, " +
            "CASE WHEN bm.userRecommendListId IS NOT NULL THEN TRUE ELSE FALSE END) " +
            "from UserRecommendList url " +
            "inner join User u " +
            "on u.id=url.userId " +
            "left join BookMark bm " +
            "on bm.userId = :userId AND bm.userRecommendListId = url.id " +
            "where url.title like %:keyword%")
    Page<RecommendListWithBookMarkDto> findUserRecommendListByKeyword(String keyword, Long userId, Pageable pageable);
}
