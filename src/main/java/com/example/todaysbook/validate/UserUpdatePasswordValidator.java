package com.example.todaysbook.validate;

import com.example.todaysbook.constant.Constant;
import com.example.todaysbook.domain.dto.UserRequestDto;
import com.example.todaysbook.exception.user.EmptyPasswordException;
import com.example.todaysbook.exception.user.WrongPasswordPatternException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UserUpdatePasswordValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return UserRequestDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserRequestDto request = (UserRequestDto) target;

        String password = request.getPassword();

        if(isPasswordEmpty(password)) {
            throw new EmptyPasswordException();
        }
        if(isWrongPasswordPattern(password)) {
            throw new WrongPasswordPatternException();
        }
    }

    private boolean isPasswordEmpty(String password) {
        return password.isEmpty();
    }

    private boolean isWrongPasswordPattern(String password) {
        return !(Constant.PASSWORD_PATTERN.matcher(password).matches());
    }
}
