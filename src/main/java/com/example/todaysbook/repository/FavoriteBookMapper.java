package com.example.todaysbook.repository;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FavoriteBookMapper {

    int addFavoriteBook(long userId, long bookId);
    int deleteFavoriteBook(long userId, long bookId);
}
