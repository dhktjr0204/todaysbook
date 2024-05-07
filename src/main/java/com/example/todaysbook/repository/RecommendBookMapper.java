package com.example.todaysbook.repository;

import com.example.todaysbook.domain.dto.RecommendBookDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RecommendBookMapper {

    List<RecommendBookDto> getRecommendBooks(Long bookIds);
    int truncateRecommendBook();
    int insertRecommendBookInfo(List<Map<String, Object>> bookInfos);
}
