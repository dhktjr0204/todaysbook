package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.RecommendBookDto;
import com.example.todaysbook.repository.RecommendBookMapper;
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
public class RecommendBookServiceImplTest {

    @InjectMocks
    private RecommendBookServiceImpl recommendBookService;

    @Mock
    private RecommendBookMapper recommendBookMapper;

    @Test
    @DisplayName("추천 책 가져오기 테스트")
    public void getRecommendBooksTest() {

        //given
        Long bookId = 1L;

        List<RecommendBookDto> dummyList = Arrays.asList(
                RecommendBookDto.builder()
                        .id(1L)
                        .title("test")
                        .price(10000)
                        .author("author")
                        .imagePath("imagePath")
                        .build()
        );

        doReturn(dummyList)
                .when(recommendBookMapper)
                .getRecommendBooks(bookId);

        //when
        List<RecommendBookDto> recommendBooks = recommendBookService.getRecommendBooks(bookId);

        //then
        assertEquals(1, recommendBooks.size());

        verify(recommendBookMapper, times(1))
                .getRecommendBooks(bookId);
    }
}
