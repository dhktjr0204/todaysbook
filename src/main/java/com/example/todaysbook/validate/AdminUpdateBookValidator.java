package com.example.todaysbook.validate;

import com.example.todaysbook.constant.Constant;
import com.example.todaysbook.domain.dto.BookDto;
import com.example.todaysbook.domain.dto.RecommendListCreateRequestDto;
import com.example.todaysbook.exception.admin.AuthorLengthOverException;
import com.example.todaysbook.exception.admin.DescriptionLengthOverException;
import com.example.todaysbook.exception.admin.EmptyAuthorException;
import com.example.todaysbook.exception.admin.EmptyPriceException;
import com.example.todaysbook.exception.admin.EmptyPublisherException;
import com.example.todaysbook.exception.admin.PublisherLengthOverException;
import com.example.todaysbook.exception.recommendList.EmptyTitleException;
import com.example.todaysbook.exception.recommendList.RecommendListLengthOverException;
import com.example.todaysbook.exception.recommendList.TitleLengthOverException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.awt.print.Book;
import java.util.List;

public class AdminUpdateBookValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return BookDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BookDto request = (BookDto) target;

        String title = request.getTitle();
        String author = request.getAuthor();
        String publisher = request.getPublisher();
        String description = request.getDescription();
        Long price=request.getPrice();

        if(isTitleEmpty(title)){
            throw new EmptyTitleException();
        }
        if(isTitleLengthOver(title)){
            throw new TitleLengthOverException();
        }
        if(isAuthorEmpty(author)){
            throw new EmptyAuthorException();
        }
        if(isAuthorLengthOver(author)){
            throw new AuthorLengthOverException();
        }
        if(isPublisherEmpty(publisher)){
            throw new EmptyPublisherException();
        }
        if(isPublisherLengthOver(publisher)){
            throw new PublisherLengthOverException();
        }
        if(isDescriptionLengthOver(description)){
            throw new DescriptionLengthOverException();
        }
        if(isPriceEmpty(price)){
            throw new EmptyPriceException();
        }
    }

    private boolean isTitleEmpty(String title){
        return title.isEmpty();
    }

    private boolean isTitleLengthOver(String title){
        return title.length()> Constant.BOOK_TITLE_MAX_LENGTH;
    }

    private boolean isAuthorEmpty(String author){
        return author.isEmpty();
    }

    private boolean isAuthorLengthOver(String author){
        return author.length()>Constant.AUTHOR_MAX_LENGTH;
    }

    private boolean isPublisherEmpty(String publisher){
        return publisher.isEmpty();
    }

    private boolean isPublisherLengthOver(String publisher){
        return publisher.length()>Constant.PUBLISHER_MAX_LENGTH;
    }

    private boolean isDescriptionLengthOver(String description){
        return description.length()>Constant.DESCRIPTION_MAX_LENGTH;
    }

    private boolean isPriceEmpty(Long price){return price==null;}
}
