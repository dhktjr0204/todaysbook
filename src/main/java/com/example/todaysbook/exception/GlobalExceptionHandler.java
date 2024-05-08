package com.example.todaysbook.exception;

import com.example.todaysbook.exception.book.DuplicateBookException;
import com.example.todaysbook.exception.bookMark.AlreadyBookmarkedException;
import com.example.todaysbook.exception.bookMark.NotBookmarkedYetException;
import com.example.todaysbook.exception.recommendList.BookNotFoundException;
import com.example.todaysbook.exception.recommendList.EmptyTitleException;
import com.example.todaysbook.exception.recommendList.RecommendListLengthOverException;
import com.example.todaysbook.exception.recommendList.RecommendListNotFoundException;
import com.example.todaysbook.exception.recommendList.TitleLengthOverException;
import com.example.todaysbook.exception.user.NotLoggedInException;
import com.example.todaysbook.exception.user.UserNotFoundException;
import com.example.todaysbook.exception.user.UserValidateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    //유저 관련 예외처리
    @ExceptionHandler(NotLoggedInException.class)
    public ResponseEntity<String> NotLoggedInException(NotLoggedInException e){
        return new ResponseEntity<>("로그인을 해주세요.", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserValidateException.class)
    public ResponseEntity<String> UserValidateException(UserValidateException e){
        return new ResponseEntity<>("인증되지 않은 유저입니다.", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> UserNotFoundException(UserNotFoundException e){
        return new ResponseEntity<>("해당 유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }

    //책 관련 예외처리
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<String> BookNotFoundException(BookNotFoundException e){
        return new ResponseEntity<>("해당 책을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateBookException.class)
    public ResponseEntity<String> DuplicateBookException(DuplicateBookException e){
        return new ResponseEntity<>("이미 등록된 책이 있습니다.", HttpStatus.BAD_REQUEST);
    }

    //추천 리스트 예외처리
    @ExceptionHandler(RecommendListNotFoundException.class)
    public ResponseEntity<String> RecommendListNotFoundException(RecommendListNotFoundException e){
        return new ResponseEntity<>("리스트를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TitleLengthOverException.class)
    public ResponseEntity<String> TitleLengthOverException(TitleLengthOverException e){
        return new ResponseEntity<>("리스트 제목 길이를 수정해주세요.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyTitleException.class)
    public ResponseEntity<String> EmptyTitleException(EmptyTitleException e){
        return new ResponseEntity<>("리스트 제목을 작성해주세요.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RecommendListLengthOverException.class)
    public ResponseEntity<String> RecommendListLengthOverException(RecommendListLengthOverException e){
        return new ResponseEntity<>("추천 리스트는 10개 이하로 구성해주세요.", HttpStatus.BAD_REQUEST);
    }

    //북마크 관련 예외처리
    @ExceptionHandler(AlreadyBookmarkedException.class)
    public ResponseEntity<String> AlreadyBookmarkedException(AlreadyBookmarkedException ex) {
        return new ResponseEntity<>("해당 게시물은 이미 북마크로 등록이 되어있습니다.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotBookmarkedYetException.class)
    public ResponseEntity<String> NotBookmarkedYetException(NotBookmarkedYetException ex) {
        return new ResponseEntity<>("해당 게시물은 이미 북마크로 등록이 되어있지 않습니다.",HttpStatus.BAD_REQUEST);
    }

}