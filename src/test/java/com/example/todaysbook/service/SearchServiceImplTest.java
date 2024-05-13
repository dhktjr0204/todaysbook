package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.BookDto;
import com.example.todaysbook.domain.dto.RecommendListDetailWithBookMarkDto;
import com.example.todaysbook.domain.dto.RecommendListWithBookMarkDto;
import com.example.todaysbook.domain.entity.Book;
import com.example.todaysbook.repository.BookRepository;
import com.example.todaysbook.repository.RecommendListMapper;
import com.example.todaysbook.repository.UserRecommendListRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class SearchServiceImplTest {
    @InjectMocks
    private SearchServiceImpl searchService;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private UserRecommendListRepository userRecommendListRepository;
    @Mock
    private RecommendListMapper recommendListMapper;

    private Book createDummyBook(Long id, String title, Long price) {
        return Book.builder()
                .id(id)
                .title(title)
                .price(price)
                .build();
    }

    private RecommendListWithBookMarkDto createDummyListDto(Long id, String title) {
        return RecommendListWithBookMarkDto.builder()
                .listId(id)
                .listTitle(title)
                .build();
    }

    private BookDto createDummyBookDto(Long id, String title, Long price) {
        return BookDto.builder()
                .id(id)
                .title(title)
                .price(price)
                .build();
    }

    @Test
    @DisplayName("책 검색 테스트")
    public void searchBookTest() {
        //given
        String keyword = "book";
        Sort sort = Sort.by(
                Sort.Order.asc("publishDate"),
                Sort.Order.asc("title")
        );
        Pageable pageable = PageRequest.of(0, 10, sort);

        List<Book> bookList = Arrays.asList(
                createDummyBook(1L, "book1", 10000L),
                createDummyBook(2L, "book2", 10000L),
                createDummyBook(3L, "book3", 10000L)
        );
        Page<Book> bookPage = new PageImpl<>(bookList);

        when(bookRepository.findByAuthorContainingOrTitleContaining(keyword, keyword, pageable))
                .thenReturn(bookPage);

        //when
        Page<BookDto> bookDtos = searchService.searchBookByKeyword(keyword, pageable);

        //then
        verify(bookRepository, times(1)).findByAuthorContainingOrTitleContaining(keyword, keyword, pageable);
        assertEquals(3, bookDtos.getContent().size());
    }

    @Test
    @DisplayName("검색결과가 아무것도 없을 때 책 검색 테스트")
    public void searchBook_withNoBookTest() {
        //given
        String keyword = "book";
        Sort sort = Sort.by(
                Sort.Order.asc("publishDate"),
                Sort.Order.asc("title")
        );
        Pageable pageable = PageRequest.of(0, 10, sort);

        List<Book> bookList = new ArrayList<>();
        Page<Book> bookPage = new PageImpl<>(bookList);

        when(bookRepository.findByAuthorContainingOrTitleContaining(keyword, keyword, pageable))
                .thenReturn(bookPage);

        //when
        Page<BookDto> bookDtos = searchService.searchBookByKeyword(keyword, pageable);

        //then
        assertTrue(bookDtos.isEmpty());
    }

    @Test
    @DisplayName("리스트 검색 테스트")
    public void SearchListTest() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        String keyword = "list";
        Long userId = 1L;

        List<BookDto> books = Arrays.asList(
                createDummyBookDto(1L, "book1", 1000L),
                createDummyBookDto(2L, "book2", 1000L),
                createDummyBookDto(3L, "book3", 1000L)
        );

        List<RecommendListWithBookMarkDto> list = Arrays.asList(
                createDummyListDto(1L, "list1"),
                createDummyListDto(2L, "list2"),
                createDummyListDto(3L, "list3")
        );

        Page<RecommendListWithBookMarkDto> bookPage = new PageImpl<>(list);

        when(userRecommendListRepository.findUserRecommendListByKeyword(keyword, userId, pageable))
                .thenReturn(bookPage);
        when(recommendListMapper.getBookDetailByListId(any())).thenReturn(books);

        //when
        searchService.searchListByKeyword(keyword, userId, pageable);

        //then
        verify(userRecommendListRepository, times(1)).findUserRecommendListByKeyword(keyword, userId, pageable);
        verify(recommendListMapper, times(3)).getBookDetailByListId(any());
    }

    @Test
    @DisplayName("검색 결과가 없을때 리스트 테스트")
    public void SearchList_withNoListTest() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        String keyword = "list";
        Long userId = 1L;

        List<RecommendListWithBookMarkDto> list = new ArrayList<>();

        Page<RecommendListWithBookMarkDto> bookPage = new PageImpl<>(list);

        when(userRecommendListRepository.findUserRecommendListByKeyword(keyword, userId, pageable))
                .thenReturn(bookPage);

        //when
        Page<RecommendListDetailWithBookMarkDto> result = searchService.searchListByKeyword(keyword, userId, pageable);

        //then
        assertTrue(result.isEmpty());
    }
}