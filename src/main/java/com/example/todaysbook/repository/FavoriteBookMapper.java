package com.example.todaysbook.repository;

import com.example.todaysbook.domain.dto.FavoriteBookDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FavoriteBookMapper {

    int addFavoriteBook(Long userId, Long bookId);
    int deleteFavoriteBook(Long userId, Long bookId);
    List<FavoriteBookDTO> getRecommendBooksByFavoriteBooks(Long userId);
}
