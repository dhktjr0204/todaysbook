package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.RecommendListDetailDto;

public interface RecommendListService {
    RecommendListDetailDto getRecommendListDetail(Long listId);
}
