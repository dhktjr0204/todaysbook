package com.example.todaysbook.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum Role {

    ROLE_BRONZE("BRONZE"),
    ROLE_SILVER("SILVER"),
    ROLE_GOLD("GOLD"),
    ROLE_DIA("DIAMOND"),
    ROLE_ADMIN("ADMIN");

    private String name;
}
