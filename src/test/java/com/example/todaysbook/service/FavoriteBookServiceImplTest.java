package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.FavoriteBookDTO;
import com.example.todaysbook.repository.FavoriteBookMapper;
import com.example.todaysbook.repository.FavoriteBookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FavoriteBookServiceImplTest {

    @InjectMocks
    private FavoriteBookServiceImpl favoriteBookService;

    @Mock
    private FavoriteBookMapper favoriteBookMapper;

    @Mock
    private FavoriteBookRepository favoriteBookRepository;

    @Test
    @DisplayName("책 찜하기 테스트")
    public void addFavoriteBook() {

        //given
        Long bookId = 1L;
        Long userId = 1L;

        doReturn(1)
                .when(favoriteBookMapper)
                .addFavoriteBook(userId, bookId);

        //when
        int result = favoriteBookService.addFavoriteBook(userId, bookId);

        //then
        assertEquals(1, result);
        verify(favoriteBookMapper, times(1)).addFavoriteBook(userId, bookId);
    }

    @Test
    @DisplayName("책 찜하기 취소 테스트")
    public void deleteFavoriteBook() {

        //given
        Long bookId = 1L;
        Long userId = 1L;

        doReturn(1)
                .when(favoriteBookMapper)
                .deleteFavoriteBook(userId, bookId);

        //when
        int result = favoriteBookService.deleteFavoriteBook(userId, bookId);

        //then
        assertEquals(1, result);
        verify(favoriteBookMapper, times(1)).deleteFavoriteBook(userId, bookId);
    }

    @Test
    @DisplayName("찜한 책 가져오기 테스트")
    public void getFavoriteBooks() {

        //given
        Long userId = 1L;
        List<FavoriteBookDTO> favoriteBooks = Arrays.asList(
                FavoriteBookDTO.builder().id(1L).title("Book 1").build(),
                FavoriteBookDTO.builder().id(2L).title("Book 2").build()
        );
        doReturn(favoriteBooks)
                .when(favoriteBookRepository)
                .findFavoriteBooksByUserId(userId);

        //when
        List<FavoriteBookDTO> result = favoriteBookService.getFavoriteBooks(userId);

        //then
        assertEquals(favoriteBooks.size(), result.size());
        verify(favoriteBookRepository, times(1)).findFavoriteBooksByUserId(userId);
    }
}
