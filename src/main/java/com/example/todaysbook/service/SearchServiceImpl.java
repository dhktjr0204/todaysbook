package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.BookDto;
import com.example.todaysbook.domain.entity.Book;
import com.example.todaysbook.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService{
    private final BookRepository bookRepository;

    @Override
    public Page<BookDto> searchByKeyword(String keyword, Pageable pageable) {
        Page<Book> searchResult = bookRepository.findByAuthorContainingOrTitleContaining(keyword, keyword, pageable);

        if(searchResult.isEmpty()){
            return Page.empty();
        }

        return searchResult.map(this::convertBookDto);
    }

    private BookDto convertBookDto(Book book){
        BookDto result= BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .price(book.getPrice())
                .image(book.getImagePath())
                .build();

        return result;
    }
}
