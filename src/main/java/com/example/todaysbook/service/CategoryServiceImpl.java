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
public class CategoryServiceImpl implements CategoryService{

    private final BookRepository bookRepository;

    @Override
    public Page<BookDto> getBooksByCategoryId(String categoryId, Pageable pageable) {
        Page<Book> allByCategoryId = bookRepository.findAllByCategoryId(categoryId, pageable);

        return allByCategoryId.map(this::convertToBookDto);
    }

    @Override
    public Page<BookDto> getBooksSortByBestSeller(String categoryId, Pageable pageable) {
        return bookRepository.findBookSortByBestSeller(categoryId, pageable);
    }

    @Override
    public Page<BookDto> getBooksSortByReviewScore(String categoryId, Pageable pageable) {
        return bookRepository.findBookSortByReviewScore(categoryId, pageable);
    }

    private BookDto convertToBookDto(Book book){
        return BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .price(book.getPrice())
                .image(book.getImagePath())
                .build();
    }
}
