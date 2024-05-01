package com.example.todaysbook.repository;

import com.example.todaysbook.domain.entity.Cart;
import com.example.todaysbook.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findById(long id);
}