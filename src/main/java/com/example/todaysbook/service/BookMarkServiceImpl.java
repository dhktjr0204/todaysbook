package com.example.todaysbook.service;

import com.example.todaysbook.domain.entity.BookMark;
import com.example.todaysbook.repository.BookMarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookMarkServiceImpl implements BookMarkService{
    private final BookMarkRepository bookMarkRepository;

    @Transactional
    @Override
    public void addMark(Long userId, Long listId) {
        Optional<BookMark> bookMark = bookMarkRepository.findByUserIdAndUserRecommendListId(userId, listId);

        if(bookMark.isPresent()){//이미 사용자가 해당 리스트를 북마크해놓았다면
            //예외처리
        }

        BookMark saveBookmark=BookMark.builder().userRecommendListId(listId).userId(userId).build();
        bookMarkRepository.save(saveBookmark);
    }

    @Transactional
    @Override
    public void cancelMark(Long userId, Long listId) {
        Optional<BookMark> bookMark = bookMarkRepository.findByUserIdAndUserRecommendListId(userId, listId);

        if(bookMark.isEmpty()){//사용자가 해당 리스트를 북마크로 등록하지 않았다면
            //예외처리
        }

        bookMarkRepository.delete(bookMark.get());
    }
}
