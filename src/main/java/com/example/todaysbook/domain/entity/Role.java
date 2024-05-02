package com.example.todaysbook.domain.entity;

public enum Role {
    ROLE_COMMON_BRONZE("BRONZE"),
    ROLE_COMMON_SILVER("SILVER"),
    ROLE_COMMON_GOLD("GOLD"),
    ROLE_COMMON_DIAMOND("DIA"),
    ROLE_ADMIN("ADMIN");

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
