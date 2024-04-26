package com.example.todaysbook.repository;

import com.example.todaysbook.domain.dto.BookDto;
import com.example.todaysbook.domain.dto.RecommendListDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RecommendListMapper {
    RecommendListDto getRecommendListByListId(Long listId);
    List<BookDto> getBookDetailByListId(Long listId);
}
