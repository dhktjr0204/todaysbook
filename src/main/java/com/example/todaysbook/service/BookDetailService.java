package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.BookDetailDto;

public interface BookDetailService {

    BookDetailDto getBookDetail(long bookId, long userId);
}
