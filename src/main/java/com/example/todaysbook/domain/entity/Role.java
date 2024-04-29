package com.example.todaysbook.domain.entity;

public enum Role {
    ROLE_COMMON_BRONZE("브론즈"),
    ROLE_COMMON_SILVER("실버"),
    ROLE_COMMON_GOLD("골드"),
    ROLE_ADMIN("관리자");

    String role;

    Role(String role) {
        this.role = role;
    }

    public String value() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
