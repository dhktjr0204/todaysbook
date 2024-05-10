package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.FavoriteBookDTO;
import com.example.todaysbook.repository.FavoriteBookMapper;
import com.example.todaysbook.repository.FavoriteBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteBookServiceImpl implements FavoriteBookService {

    private final FavoriteBookMapper favoriteBookMapper;
    private final FavoriteBookRepository favoriteBookRepository;
    @Override
    public Page<FavoriteBookDTO> getFavoriteBooks(Long userId, Pageable pageable) {

        return favoriteBookRepository.findFavoriteBooksByUserId(userId, pageable);
    }

    @Override
    public List<FavoriteBookDTO> getFavoriteBooks(Long userId) {

        return favoriteBookRepository.findFavoriteBooksByUserId(userId);
    }

    @Override
    public List<FavoriteBookDTO> getRecommendBooksByFavoriteBooks(Long userId) {

        return favoriteBookMapper.getRecommendBooksByFavoriteBooks(userId);
    }

    @Override
    public int addFavoriteBook(Long userId, Long bookId) {

        return favoriteBookMapper.addFavoriteBook(userId, bookId);
    }

    @Override
    public int deleteFavoriteBook(Long userId, Long bookId) {

        return favoriteBookMapper.deleteFavoriteBook(userId, bookId);
    }
}
