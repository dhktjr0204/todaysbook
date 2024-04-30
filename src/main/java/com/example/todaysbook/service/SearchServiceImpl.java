package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.BookDto;
import com.example.todaysbook.domain.dto.RecommendListDetailDto;
import com.example.todaysbook.domain.dto.RecommendListDto;
import com.example.todaysbook.domain.entity.Book;
import com.example.todaysbook.repository.BookRepository;
import com.example.todaysbook.repository.RecommendListMapper;
import com.example.todaysbook.repository.UserRecommendListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService{
    private final BookRepository bookRepository;
    private final UserRecommendListRepository userRecommendListRepository;
    private final RecommendListMapper recommendListMapper;

    @Override
    public Page<BookDto> searchBookByKeyword(String keyword, Pageable pageable) {
        Page<Book> searchResult = bookRepository.findByAuthorContainingOrTitleContaining(keyword, keyword, pageable);

        if(searchResult.isEmpty()){
            return Page.empty();
        }

        return searchResult.map(this::convertBookDto);
    }
    @Override
    public Page<RecommendListDetailDto> searchListByKeyword(String keyword, Pageable pageable) {
        Page<RecommendListDto> searchResult= userRecommendListRepository.findUserRecommendListByKeyword(keyword, pageable);

        if(searchResult.isEmpty()){
            return Page.empty();
        }

        List<RecommendListDetailDto> resultList = new ArrayList<>();
        for(RecommendListDto list:searchResult){
            List<BookDto> bookList = recommendListMapper.getBookDetailByListId(list.getListId());
            RecommendListDetailDto recommendListDetailDto = convertListDetailDto(list, bookList);
            resultList.add(recommendListDetailDto);
        }

        return new PageImpl<>(resultList, pageable, searchResult.getTotalElements());
    }

    private BookDto convertBookDto(Book book){
        return BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .price(book.getPrice())
                .image(book.getImagePath())
                .build();
    }

    private RecommendListDetailDto convertListDetailDto(RecommendListDto list, List<BookDto> book){
        return RecommendListDetailDto.builder()
                .listId(list.getListId())
                .listTitle(list.getListTitle())
                .userId(list.getUserId())
                .nickname(list.getNickname())
                .date(list.getDate())
                .bookList(book)
                .build();
    }
}
