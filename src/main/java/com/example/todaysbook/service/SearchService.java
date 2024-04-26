package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.BookDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface SearchService {
    Page<BookDto> searchByKeyword(String keyword, Pageable pageable);
}
