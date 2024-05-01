package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.BookDetail;
import com.example.todaysbook.domain.dto.BookDetailDto;
import com.example.todaysbook.domain.dto.Review;
import com.example.todaysbook.repository.BookDetailMapper;
import com.example.todaysbook.repository.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookDetailServiceImpl implements BookDetailService {

    private final BookDetailMapper bookDetailMapper;
    private final ReviewMapper reviewMapper;
    @Override
    public BookDetailDto getBookDetail(long bookId, long userId) {

        BookDetail bookDetail = bookDetailMapper.getBookDetail(bookId, userId);
        List<Review> reviews = reviewMapper.getReviews(bookId, userId);

        return new BookDetailDto().builder()
                .bookDetail(bookDetail)
                .reviews(reviews)
                .build();
    }
}
