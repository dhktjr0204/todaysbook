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
    BookDto findBookById(Long bookId);
    void updateUserRole(Long userId, String role);
    void deleteUser(Long userId);
    void updateBookStock(Long bookId, Long stock);
    void deleteBook(Long bookId);
    void updateBook(BookDto bookDto);
}
