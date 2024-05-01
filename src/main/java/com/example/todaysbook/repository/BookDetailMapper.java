package com.example.todaysbook.repository;

import com.example.todaysbook.domain.dto.BookDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookDetailMapper {

    BookDetail getBookDetail(long bookId, long userId);
}
