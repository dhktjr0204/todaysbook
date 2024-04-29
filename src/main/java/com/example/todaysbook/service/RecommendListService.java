package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.RecommendListCreateRequestDto;
import com.example.todaysbook.domain.dto.RecommendListUpdateRequestDto;
import com.example.todaysbook.domain.dto.RecommendListDetailDto;
import com.example.todaysbook.domain.entity.UserRecommendList;

public interface RecommendListService {
    RecommendListDetailDto getRecommendListDetail(Long listId);
    UserRecommendList save(RecommendListCreateRequestDto request);
    void update(RecommendListUpdateRequestDto request);
    void delete(Long listId);
}
