package com.example.todaysbook.validate;

import com.example.todaysbook.domain.dto.RecommendListCreateRequestDto;
import com.example.todaysbook.exception.recommendList.EmptyTitleException;
import com.example.todaysbook.exception.recommendList.RecommendListLengthOverException;
import com.example.todaysbook.exception.recommendList.TitleLengthOverException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

public class RecommendListCreateValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return RecommendListCreateRequestDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RecommendListCreateRequestDto request=(RecommendListCreateRequestDto) target;

        String listTitle=request.getTitle();
        List<Long> bookList=request.getBookIdList();

        if(isTitleEmpty(listTitle)){
            throw new EmptyTitleException();
        }
        if(isTitleLengthLimitOver(listTitle)){
            throw new TitleLengthOverException();
        }
        if(isListCountLimitOver(bookList)){
            throw new RecommendListLengthOverException();
        }

    }

    private boolean isTitleEmpty(String title){
        return title.isEmpty();
    }

    private boolean isTitleLengthLimitOver(String title){
        return title.length()>30;
    }

    private boolean isListCountLimitOver(List<Long> booklist){
        if(booklist!=null){
            return booklist.size()>10;
        }
        return false;
    }
}
