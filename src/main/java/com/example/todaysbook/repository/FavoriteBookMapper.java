package com.example.todaysbook.repository;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FavoriteBookMapper {

    int addFavoriteBook(Long userId, Long bookId);
    int deleteFavoriteBook(Long userId, Long bookId);
}
