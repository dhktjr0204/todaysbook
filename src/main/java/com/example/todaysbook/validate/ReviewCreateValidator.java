package com.example.todaysbook.validate;

import com.example.todaysbook.domain.dto.ReviewRequestDto;
import com.example.todaysbook.exception.review.ContentEmptyException;
import com.example.todaysbook.exception.review.ContentLengthOverException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ReviewCreateValidator implements Validator {

    private int CONTENT_MAX_LENGTH = 500;

    @Override
    public boolean supports(Class<?> clazz) {
        return ReviewRequestDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        ReviewRequestDto requestDto = (ReviewRequestDto) target;

        if(isEmpty(requestDto.getContent())) {

            throw new ContentEmptyException();
        }

        if(isContentLengthOver(requestDto.getContent())) {

            throw new ContentLengthOverException();
        }
    }

    private boolean isEmpty(String content) {

        return content.isEmpty();
    }

    private boolean isContentLengthOver(String content) {

        return content.length() > CONTENT_MAX_LENGTH;
    }
}
