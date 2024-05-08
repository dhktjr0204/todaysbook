package com.example.todaysbook.repository;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    void updateUserRole();
}
