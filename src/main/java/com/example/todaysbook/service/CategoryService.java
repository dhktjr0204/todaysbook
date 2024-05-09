package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.BookDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Page<BookDto> getBooksByCategoryId(String categoryId, Pageable pageable);
    Page<BookDto> getBooksSortByBestSeller(String categoryId, Pageable pageable);
    Page<BookDto> getBooksSortByReviewScore(String categoryId, Pageable pageable);
}
