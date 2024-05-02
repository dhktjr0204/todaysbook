package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.AdminUserDto;
import com.example.todaysbook.domain.dto.BookDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {
    Page<AdminUserDto> findAllUser(Pageable pageable);
    Page<AdminUserDto> findUsersByKeyword(String keyword, Pageable pageable);
    Page<BookDto> findAllBook(Pageable pageable);
    Page<BookDto> findBooksByKeyword(String keyword, Pageable pageable);
}
