package com.example.todaysbook.service;

public interface FavoriteBookService {

    int addFavoriteBook(long userId, long bookId);
    int deleteFavoriteBook(long userId, long bookId);
}
