package com.example.todaysbook.util;

import com.example.todaysbook.domain.dto.CustomUserDetails;

public class UserChecker {

    public static Long getUserId(CustomUserDetails userDetails) {

        return userDetails == null ? 0 : userDetails.getUserId();
    }
}
