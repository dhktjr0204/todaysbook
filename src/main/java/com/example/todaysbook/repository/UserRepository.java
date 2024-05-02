package com.example.todaysbook.repository;

import com.example.todaysbook.domain.entity.Cart;
import com.example.todaysbook.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findById(long id);
    Page<User> findAll(Pageable pageable);
    Page<User> findByEmailContainingOrNickNameContaining(String email, String nickName, Pageable pageable);
}