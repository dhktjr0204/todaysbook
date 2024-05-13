package com.example.todaysbook.validate;

import com.example.todaysbook.constant.Constant;
import com.example.todaysbook.domain.dto.UserRequestDto;
import com.example.todaysbook.exception.user.EmptyNicknameException;
import com.example.todaysbook.exception.user.NicknameLengthOverException;
import com.example.todaysbook.exception.user.NicknameLengthUnderException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UserUpdateNicknameValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return UserRequestDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserRequestDto request = (UserRequestDto) target;
        String nickname = request.getNickName();

        if(isEmptyNickname(nickname)) {
            throw new EmptyNicknameException();
        }
        if(isNicknameLengthUnder(nickname)) {
            throw new NicknameLengthUnderException();
        }
        if(isNicknameLengthOver(nickname)) {
            throw new NicknameLengthOverException();
        }
    }

    private boolean isEmptyNickname(String nickname) {
        return nickname.isEmpty();
    }

    private boolean isNicknameLengthUnder(String nickname) {
        return nickname.length() < Constant.NICKNAME_MIN_LENGTH;
    }

    private boolean isNicknameLengthOver(String nickname) {
        return nickname.length() > Constant.NICKNAME_MAX_LENGTH;
    }
}
