package com.example.todaysbook.service;

import com.example.todaysbook.repository.FavoriteBookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteBookServiceImpl implements FavoriteBookService {

    private final FavoriteBookMapper favoriteBookMapper;

    @Override
    public int addFavoriteBook(long userId, long bookId) {

        return favoriteBookMapper.addFavoriteBook(userId, bookId);
    }

    @Override
    public int deleteFavoriteBook(long userId, long bookId) {

        return favoriteBookMapper.deleteFavoriteBook(userId, bookId);
    }
}
