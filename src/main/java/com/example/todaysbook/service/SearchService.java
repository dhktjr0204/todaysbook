package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.BookDto;
import com.example.todaysbook.domain.dto.RecommendListDetailWithBookMarkDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface SearchService {
    Page<BookDto> searchBookByKeyword(String keyword, Pageable pageable);
    Page<RecommendListDetailWithBookMarkDto> searchListByKeyword(String keyword, Long userId, Pageable pageable);
}
