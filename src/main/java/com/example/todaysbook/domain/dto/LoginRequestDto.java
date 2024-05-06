package com.example.todaysbook.domain.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;
    private String password;
}
