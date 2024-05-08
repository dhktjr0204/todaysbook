package com.example.todaysbook.domain.entity;

import com.example.todaysbook.domain.dto.UserResponseDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String nickName;
    private String email;
    private String password;
    private String role;
    private String mileage;
    private java.sql.Timestamp expire;
    private boolean is_expired;
    private String address;
    private String zipcode;

    public UserResponseDto toResponse() {
        return UserResponseDto
                .builder()
                .id(id)
                .email(email)
                .nickName(nickName)
                .build();
    }
  
    public void updateRole(String role){
        this.role=role;
    }
}
