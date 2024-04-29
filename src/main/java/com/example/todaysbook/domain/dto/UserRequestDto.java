package com.example.todaysbook.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    private Long id;
    private String name;
    private String nickName;
    private String email;
    private String password;
    private String role;
    private String address;
    private String zipcode;
}
