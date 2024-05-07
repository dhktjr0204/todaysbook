package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.FavoriteBookDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FavoriteBookService {

    Page<FavoriteBookDTO> getFavoriteBooks(long userId, Pageable pageable);
    List<FavoriteBookDTO> getFavoriteBooks(long userId);
    int addFavoriteBook(long userId, long bookId);
    int deleteFavoriteBook(long userId, long bookId);
}
