package com.example.todaysbook.validate;

import com.example.todaysbook.constant.Constant;
import com.example.todaysbook.domain.dto.UserRequestDto;
import com.example.todaysbook.exception.user.EmptyAddressException;
import com.example.todaysbook.exception.user.EmptyZipcodeException;
import com.example.todaysbook.exception.user.ZipcodeNotANumberException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UserUpdateAddressValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return UserRequestDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserRequestDto request = (UserRequestDto) target;

        String address = request.getAddress();
        String zipcode = request.getZipcode();

        if(isAddressEmpty(address)) {
            throw new EmptyAddressException();
        }
        if(isZipcodeEmpty(zipcode)) {
            throw new EmptyZipcodeException();
        }
        if(isZipcodeNotANumber(zipcode)) {
            throw new ZipcodeNotANumberException();
        }
    }

    private boolean isAddressEmpty(String address) {
        return address.isEmpty();
    }

    private boolean isZipcodeEmpty(String zipcode) {
        return zipcode.isEmpty();
    }

    private boolean isZipcodeNotANumber(String zipcode) {
        return !(Constant.ZIPCODE_PATTERN.matcher(zipcode).matches());
    }
}
