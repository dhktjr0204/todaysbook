package com.example.todaysbook.service;

import com.example.todaysbook.domain.entity.BookMark;
import com.example.todaysbook.exception.bookMark.AlreadyBookmarkedException;
import com.example.todaysbook.exception.bookMark.NotBookmarkedYetException;
import com.example.todaysbook.repository.BookMarkRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookMarkServiceImplTest {
    @InjectMocks
    private BookMarkServiceImpl bookMarkService;
    @Mock
    private BookMarkRepository bookMarkRepository;

    @Test
    @DisplayName("북마크 저장 테스트")
    public void addMarkTest() {
        //given
        Long userId = 1L;
        Long listId = 1L;

        when(bookMarkRepository.findByUserIdAndUserRecommendListId(userId, listId))
                .thenReturn(Optional.empty());

        //when
        bookMarkService.addMark(userId, listId);

        //then
        verify(bookMarkRepository).save(any(BookMark.class));
    }

    @Test
    @DisplayName("북마크 등록시 이미 북마크로 등록되어있을 때 테스트")
    public void AlreadyBookmarkedTest() {
        //given
        Long userId = 1L;
        Long listId = 1L;

        when(bookMarkRepository.findByUserIdAndUserRecommendListId(userId, listId))
                .thenReturn(Optional.of(new BookMark()));

        //when & then
        assertThrows(AlreadyBookmarkedException.class, () -> {
            bookMarkService.addMark(userId, listId);
        });
    }

    @Test
    @DisplayName("북마크 취소 테스트")
    public void cancelBookmarkTest() {
        //given
        Long userId = 1L;
        Long listId = 1L;

        BookMark bookMark = new BookMark(1L, listId, userId);

        when(bookMarkRepository.findByUserIdAndUserRecommendListId(userId, listId)).thenReturn(Optional.of(bookMark));

        //when
        bookMarkService.cancelMark(userId, listId);

        //then
        verify(bookMarkRepository).delete(bookMark);

    }

    @Test
    @DisplayName("북마크를 취소했을시 이미 북마크로 등록되어있지 않은 게시물일때 테스트")
    public void NotBookmarkedYetTest() {
        //given
        Long userId = 1L;
        Long listId = 1L;

        when(bookMarkRepository.findByUserIdAndUserRecommendListId(userId, listId)).thenReturn(Optional.empty());

        //when & then
        assertThrows(NotBookmarkedYetException.class, () -> {
            bookMarkService.cancelMark(userId, listId);
        });
    }

}