package com.example.todaysbook.domain.dto;

import com.example.todaysbook.domain.entity.User;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String email;
    private String nickName;
    private String password;
}
