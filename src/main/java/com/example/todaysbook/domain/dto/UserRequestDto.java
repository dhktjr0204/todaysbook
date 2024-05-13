package com.example.todaysbook.domain.dto;

import com.example.todaysbook.domain.entity.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    private String name;
    private String email;
    private String nickName;
    private String password;
    private String passwordCheck;
    private Role role;
    private String address;
    private String zipcode;
}
