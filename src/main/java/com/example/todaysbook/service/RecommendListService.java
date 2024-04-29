package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.RecommendListCreateRequestDto;
import com.example.todaysbook.domain.dto.RecommendListUpdateRequestDto;
import com.example.todaysbook.domain.dto.RecommendListDetailDto;
import com.example.todaysbook.domain.entity.UserRecommendList;

import java.util.List;

public interface RecommendListService {
    RecommendListDetailDto getRecommendListDetail(Long listId);
    List<RecommendListDetailDto> getMyRecommendListAll(Long userId);

    UserRecommendList save(Long userId, RecommendListCreateRequestDto request);
    void update(Long listId, RecommendListUpdateRequestDto request);
    void delete(Long listId);
}
