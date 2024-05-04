package com.example.todaysbook.exception;

import com.example.todaysbook.exception.user.NotLoggedInException;
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
}
