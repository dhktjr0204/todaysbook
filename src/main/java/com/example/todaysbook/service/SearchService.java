package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.SearchResponseDto;
import com.example.todaysbook.domain.entity.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


public interface SearchService {
    Page<SearchResponseDto> searchByKeyword(String keyword, Pageable pageable);
}
