package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.BookDto;
import com.example.todaysbook.domain.dto.RecommendListCreateRequestDto;
import com.example.todaysbook.domain.dto.RecommendListUpdateRequestDto;
import com.example.todaysbook.domain.dto.RecommendListDetailDto;
import com.example.todaysbook.domain.dto.RecommendListDto;
import com.example.todaysbook.domain.entity.UserRecommendBook;
import com.example.todaysbook.domain.entity.UserRecommendList;
import com.example.todaysbook.repository.RecommendListMapper;
import com.example.todaysbook.repository.UserRecommendBookRepository;
import com.example.todaysbook.repository.UserRecommendListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendListService {
    private final UserRecommendListRepository userRecommendListRepository;
    private final UserRecommendBookRepository userRecommendBookRepository;
    private final RecommendListMapper recommendListMapper;

    @Override
    public RecommendListDetailDto getRecommendListDetail(Long listId) {
        RecommendListDto recommendList = recommendListMapper.getRecommendListByListId(listId);
        List<BookDto> bookList = recommendListMapper.getBookDetailByListId(listId);

        return RecommendListDetailDto.builder()
                .listId(recommendList.getListId())
                .listTitle(recommendList.getListTitle())
                .userId(recommendList.getUserId())
                .nickname(recommendList.getNickname())
                .date(recommendList.getDate())
                .bookList(bookList).build();
    }

    @Transactional
    @Override
    public UserRecommendList save(RecommendListCreateRequestDto request) {

        UserRecommendList userRecommendList = UserRecommendList.builder()
                .title(request.getTitle())
                .userId(request.getUserId())
                .build();

        UserRecommendList saveUserRecommendList = userRecommendListRepository.save(userRecommendList);

        for (long bookId : request.getBookIdList()) {
            UserRecommendBook userRecommendBook = UserRecommendBook.builder()
                    .userRecommendListId(saveUserRecommendList.getId())
                    .bookId(bookId)
                    .build();

            userRecommendBookRepository.save(userRecommendBook);
        }

        return saveUserRecommendList;
    }

    @Transactional
    @Override
    public void update(RecommendListUpdateRequestDto request) {
        UserRecommendList userRecommendList = userRecommendListRepository.findById(request.getListId())
                .orElseThrow(()->new IllegalArgumentException("해당 리스트를 찾을 수 없습니다."));
        List<UserRecommendBook> userRecommendBooks = userRecommendBookRepository.findByUserRecommendListId(request.getListId())
                .orElseThrow(() -> new IllegalArgumentException("리스트에 해당되는 책을 찾을 수 없습니다."));

        //리스트에 저장된 책들 전부 삭제
        userRecommendBookRepository.deleteAll(userRecommendBooks);

        //리스트에 새로 추가
        for(Long bookId: request.getBookIdList()){
            UserRecommendBook book = UserRecommendBook.builder()
                    .userRecommendListId(request.getListId())
                    .bookId(bookId)
                    .build();

            userRecommendBookRepository.save(book);
        }

        //제목 수정
        userRecommendList.updateTitle(request.getTitle());
    }

    @Transactional
    @Override
    public void delete(Long listId) {
        UserRecommendList userRecommendList = userRecommendListRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리스트를 찾을 수 없습니다."));
        List<UserRecommendBook> userRecommendBooks = userRecommendBookRepository.findByUserRecommendListId(listId)
                .orElseThrow(() -> new IllegalArgumentException("리스트에 해당되는 책을 찾을 수 없습니다."));

        userRecommendListRepository.delete(userRecommendList);
        userRecommendBookRepository.deleteAll(userRecommendBooks);
    }
}
