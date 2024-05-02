package com.example.todaysbook.exception;




// Alan API 응답이 올바르지 않을 때 발생하는 예외
public class InvalidAlanResponseException extends Exception {
    public InvalidAlanResponseException(String message) {
        super(message);
    }

    public InvalidAlanResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}