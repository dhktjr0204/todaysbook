package com.example.todaysbook.validate;

import com.example.todaysbook.constant.Constant;
import com.example.todaysbook.domain.dto.UserRequestDto;
import com.example.todaysbook.exception.user.*;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UserRegisterValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return UserRequestDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserRequestDto request = (UserRequestDto) target;

        String name = request.getName();
        String nickname = request.getNickName();
        String email = request.getEmail();
        String password = request.getPassword();
        String passwordCheck = request.getPasswordCheck();
        String address = request.getAddress();
        String zipcode = request.getZipcode();

        if(isNameEmpty(name)) {
            throw new EmptyNameException();
        }
        if(isNicknameEmpty(nickname)) {
            throw new EmptyNicknameException();
        }
        if(isNicknameLengthUnder(nickname)) {
            throw new NicknameLengthUnderException();
        }
        if(isNicknameLengthOver(nickname)) {
            throw new NicknameLengthOverException();
        }
        if(isEmailEmpty(email)) {
            throw new EmptyEmailException();
        }
        if(isWrongEmailPattern(email)) {
            throw new WrongEmailPatternException();
        }
        if(isPasswordEmpty(password)) {
            throw new EmptyPasswordException();
        }
        if(isWrongPasswordPattern(password)) {
            throw new WrongPasswordPatternException();
        }
        if(isNotSamePassword(password, passwordCheck)) {
            throw new NotSamePasswordException();
        }
        if(isAddressEmpty(address)) {
            throw new EmptyAddressException();
        }
        if(isZipcodeEmpty(zipcode)) {
            throw new EmptyZipcodeException();
        }
        if(isWrongZipcodePattern(zipcode)) {
            throw new WrongZipcodePatternException();
        }
    }

    private boolean isNameEmpty(String name) {
        return name.isEmpty();
    }

    private boolean isNicknameEmpty(String nickname) {
        return nickname.isEmpty();
    }

    private boolean isNicknameLengthUnder(String nickname) {
        return nickname.length() < Constant.NICKNAME_MIN_LENGTH;
    }

    private boolean isNicknameLengthOver(String nickname) {
        return nickname.length() > Constant.NICKNAME_MAX_LENGTH;
    }

    private boolean isEmailEmpty(String email) {
        return email.isEmpty();
    }

    private boolean isWrongEmailPattern(String email) {
        return !(Constant.EMAIL_PATTERN.matcher(email).matches());
    }

    private boolean isPasswordEmpty(String password) {
        return password.isEmpty();
    }
    private boolean isWrongPasswordPattern(String password) {
        return !(Constant.PASSWORD_PATTERN.matcher(password).matches());
    }

    private boolean isNotSamePassword(String password, String passwordCheck) {
        return !(password.equals(passwordCheck));
    }

    private boolean isAddressEmpty(String address) {
        return address.isEmpty() || Constant.DEFAULT_ADDRESS_PATTERN.matcher(address).matches();
    }

    private boolean isZipcodeEmpty(String zipcode) {
        return zipcode.isEmpty();
    }

    private boolean isWrongZipcodePattern(String zipcode) {
        return !(Constant.ZIPCODE_PATTERN.matcher(zipcode).matches());
    }
}
