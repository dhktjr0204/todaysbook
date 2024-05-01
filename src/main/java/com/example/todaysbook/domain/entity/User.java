package com.example.todaysbook.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

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
    private String address;
    private String zipcode;
}
