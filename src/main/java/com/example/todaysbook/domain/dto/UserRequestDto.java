package com.example.todaysbook.domain.dto;

import com.example.todaysbook.domain.entity.Role;
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
    private Role role;
    private String address;
    private String zipcode;
}
