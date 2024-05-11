package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.RecommendListCreateRequestDto;
import com.example.todaysbook.domain.dto.RecommendListUpdateRequestDto;
import com.example.todaysbook.domain.entity.UserRecommendBook;
import com.example.todaysbook.domain.entity.UserRecommendList;
import com.example.todaysbook.exception.user.UserValidateException;
import com.example.todaysbook.repository.RecommendListMapper;
import com.example.todaysbook.repository.UserRecommendBookRepository;
import com.example.todaysbook.repository.UserRecommendListRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RecommendListServiceImplTest {
    @InjectMocks
    private RecommendListServiceImpl recommendService;

    @Mock
    private UserRecommendListRepository userRecommendListRepository;

    @Mock
    private UserRecommendBookRepository userRecommendBookRepository;
    @Mock
    private RecommendListMapper recommendListMapper;

    private RecommendListCreateRequestDto createDummyRecommendListCreateDto(String title, List<Long> bookList) {
        return RecommendListCreateRequestDto.builder()
                .title(title)
                .bookIdList(bookList)
                .build();
    }

    private RecommendListUpdateRequestDto createDummyRecommendListUpdateDto(Long userId, String title, List<Long> bookList) {
        return RecommendListUpdateRequestDto.builder()
                .userId(userId)
                .title(title)
                .bookIdList(bookList)
                .build();
    }

    private UserRecommendList createDummyUserRecommendList(Long listId, Long userId, String title) {
        return UserRecommendList.builder()
                .id(listId)
                .userId(userId)
                .title(title)
                .build();
    }

    private List<UserRecommendBook> createDummyUserRecommendBook(Long listId, List<Long> books) {
        List<UserRecommendBook> bookList = new ArrayList<>();

        for (Long book : books) {
            bookList.add(UserRecommendBook.builder()
                    .userRecommendListId(listId)
                    .bookId(book)
                    .build());
        }
        return bookList;
    }

    @Test
    @DisplayName("저장 테스트")
    public void addListTest() {
        //given
        Long userId = 1L;

        RecommendListCreateRequestDto request = createDummyRecommendListCreateDto("title", Arrays.asList(1L, 2L, 3L));

        when(userRecommendListRepository.save(any(UserRecommendList.class))).thenReturn(UserRecommendList.builder().id(1L).build());

        //when
        recommendService.save(userId, request);

        //then
        verify(userRecommendListRepository, times(1)).save(any(UserRecommendList.class));
        verify(userRecommendBookRepository, times(3)).save(any(UserRecommendBook.class));
    }

    @Test
    @DisplayName("수정 테스트")
    public void updateListTest() {
        //given
        Long writeUserId = 1L;
        Long currentLoginUserId = 1L;
        Long listId = 1L;

        RecommendListUpdateRequestDto request = createDummyRecommendListUpdateDto(currentLoginUserId, "title-test", Arrays.asList(2L, 3L, 4L));
        UserRecommendList userRecommendList = createDummyUserRecommendList(listId, writeUserId, "title");
        List<UserRecommendBook> userRecommendBook = createDummyUserRecommendBook(listId, Arrays.asList(1L, 2L, 3L));

        when(userRecommendListRepository.findById(any())).thenReturn(Optional.ofNullable(userRecommendList));
        when(userRecommendBookRepository.findByUserRecommendListId(any())).thenReturn(Optional.of(userRecommendBook));

        //when
        recommendService.update(listId, request);

        //then
        ArgumentCaptor<UserRecommendBook> captor = ArgumentCaptor.forClass(UserRecommendBook.class);

        //기존 책들 지우고
        verify(userRecommendBookRepository, times(1)).deleteAll(userRecommendBook);
        //새로운 책이 3개 저장됨
        verify(userRecommendBookRepository, times(3)).save(captor.capture());

        List<UserRecommendBook> saveBooks = captor.getAllValues();

        assertThat(userRecommendList.getTitle()).isEqualTo("title-test");
        assertThat(saveBooks.get(0).getBookId()).isEqualTo(2L);
        assertThat(saveBooks.get(1).getBookId()).isEqualTo(3L);
        assertThat(saveBooks.get(2).getBookId()).isEqualTo(4L);
    }

    @Test
    @DisplayName("리스트 작성한 유저가 아닌 다른 유저가 수정 요청했을 때 테스트")
    public void updateListWithNotValidUserTest() {
        Long writeUserId = 1L;
        Long currentLoginUserId = 2L;
        Long listId = 1L;

        RecommendListUpdateRequestDto request = createDummyRecommendListUpdateDto(currentLoginUserId, "title-test", Arrays.asList(2L, 3L, 4L));
        UserRecommendList userRecommendList = createDummyUserRecommendList(listId, writeUserId, "title");
        List<UserRecommendBook> userRecommendBook = createDummyUserRecommendBook(listId, Arrays.asList(1L, 2L, 3L));

        when(userRecommendListRepository.findById(any())).thenReturn(Optional.ofNullable(userRecommendList));
        when(userRecommendBookRepository.findByUserRecommendListId(any())).thenReturn(Optional.of(userRecommendBook));

        // when & then
        assertThrows(UserValidateException.class, () -> recommendService.update(listId, request));
    }

    @Test
    @DisplayName("삭제 테스트")
    public void deleteListTest() {
        //given
        Long writeUserId = 1L;
        Long listId = 1L;

        UserRecommendList userRecommendList = createDummyUserRecommendList(listId, writeUserId, "title");
        List<UserRecommendBook> userRecommendBook = createDummyUserRecommendBook(listId, Arrays.asList(1L, 2L, 3L));

        when(userRecommendListRepository.findById(any())).thenReturn(Optional.ofNullable(userRecommendList));
        when(userRecommendBookRepository.findByUserRecommendListId(any())).thenReturn(Optional.of(userRecommendBook));

        //when
        recommendService.delete(listId);

        //then
        verify(userRecommendListRepository, times(1)).delete(userRecommendList);
    }
}