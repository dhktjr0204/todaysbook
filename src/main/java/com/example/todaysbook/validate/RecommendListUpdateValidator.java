package com.example.todaysbook.validate;

import com.example.todaysbook.constant.Constant;
import com.example.todaysbook.domain.dto.RecommendListUpdateRequestDto;
import com.example.todaysbook.exception.recommendList.EmptyTitleException;
import com.example.todaysbook.exception.recommendList.RecommendListLengthOverException;
import com.example.todaysbook.exception.recommendList.TitleLengthOverException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

public class RecommendListUpdateValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return RecommendListUpdateRequestDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RecommendListUpdateRequestDto request = (RecommendListUpdateRequestDto) target;

        String listTitle = request.getTitle();
        List<Long> bookList = request.getNewBookList();

        if (isTitleEmpty(listTitle)) {
            throw new EmptyTitleException();
        }
        if (isTitleLengthOver(listTitle)) {
            throw new TitleLengthOverException();
        }
        if (isListCountOver(bookList)) {
            throw new RecommendListLengthOverException();
        }

    }

    private boolean isTitleEmpty(String title) {
        return title.isEmpty();
    }

    private boolean isTitleLengthOver(String title) {
        return title.length() > Constant.LIST_TITLE_MAX_LENGTH;
    }

    private boolean isListCountOver(List<Long> booklist) {
        if (booklist != null) {
            return booklist.size() > Constant.LIST_SIZE;
        }
        return false;
    }
}
