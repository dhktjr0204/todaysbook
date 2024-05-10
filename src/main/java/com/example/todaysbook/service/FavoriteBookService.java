package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.FavoriteBookDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FavoriteBookService {

    Page<FavoriteBookDTO> getFavoriteBooks(Long userId, Pageable pageable);
    List<FavoriteBookDTO> getFavoriteBooks(Long userId);
    List<FavoriteBookDTO> getRecommendBooksByFavoriteBooks(Long userId);
    int addFavoriteBook(Long userId, Long bookId);
    int deleteFavoriteBook(Long userId, Long bookId);
}
