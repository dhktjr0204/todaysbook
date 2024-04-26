package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.BookDto;
import com.example.todaysbook.domain.dto.RecommendListDetailDto;
import com.example.todaysbook.domain.dto.RecommendListDto;
import com.example.todaysbook.repository.RecommendListMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendListService{
    private final RecommendListMapper recommendListMapper;

    @Override
    public RecommendListDetailDto getRecommendListDetail(Long listId) {
        RecommendListDto recommendList = recommendListMapper.getRecommendListByListId(listId);
        List<BookDto> bookList=recommendListMapper.getBookDetailByListId(listId);

        return RecommendListDetailDto.builder()
                .listId(recommendList.getListId())
                .listTitle(recommendList.getListTitle())
                .userId(recommendList.getUserId())
                .nickname(recommendList.getNickname())
                .date(recommendList.getDate())
                .bookList(bookList).build();
    }
}
