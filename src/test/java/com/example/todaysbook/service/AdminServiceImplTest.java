package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.BookDto;
import com.example.todaysbook.domain.entity.Book;
import com.example.todaysbook.domain.entity.User;
import com.example.todaysbook.repository.BookRepository;
import com.example.todaysbook.repository.UserRepository;
import com.example.todaysbook.util.AladinApi;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {
    @InjectMocks
    private AdminServiceImpl adminService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private AladinApi aladinApi;

    @Test
    @DisplayName("유저 등급 변경 테스트")
    public void updateRoleTest() {
        //given
        Long userId = 1L;
        String role = "ROLE_DIAMOND";

        User user = User.builder().role("ROLE_BRONZE").build();

        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user));

        //when
        adminService.updateUserRole(userId, role);

        //then
        assertThat(user.getRole()).isEqualTo(role);
    }

    @Test
    @DisplayName("책 수량 수정 테스트")
    public void updateStockTest() {
        //given
        Long bookId = 1L;
        Long stock = 20L;

        Book book=Book.builder().stock(10L).build();

        when(bookRepository.findById(bookId)).thenReturn(Optional.ofNullable(book));

        //when
        adminService.updateBookStock(bookId, stock);

        //then
        assertThat(book.getStock()).isEqualTo(stock);
    }

    @Test
    @DisplayName("책 정보 수정 테스트")
    public void updateBookTest(){
        //given
        BookDto bookDto=BookDto.builder()
                .id(1L)
                .title("title-test")
                .stock(20L)
                .author("author-test")
                .publisher("publisher-test")
                .description("description-test")
                .build();

        Book book=Book.builder()
                .title("title")
                .stock(10L)
                .author("author")
                .publisher("publisher")
                .description("description")
                .build();

        when(bookRepository.findById(1L)).thenReturn(Optional.ofNullable(book));

        //when
        adminService.updateBook(bookDto);

        //then
        assertThat(book.getTitle()).isEqualTo(bookDto.getTitle());
        assertThat(book.getStock()).isEqualTo(bookDto.getStock());
        assertThat(book.getAuthor()).isEqualTo(bookDto.getAuthor());
        assertThat(book.getPublisher()).isEqualTo(bookDto.getPublisher());
        assertThat(book.getDescription()).isEqualTo(bookDto.getDescription());
    }
}