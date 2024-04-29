package com.example.todaysbook.repository;

import com.example.todaysbook.domain.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByNickName(String nickName);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.nickName = :nickName")
    boolean isExistNickName(@Param("nickName") String nickName);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.nickName = :nickName WHERE u.id = :id")
    void updateNickNameById(@Param("id") Long id, @Param("nickName") String nickName);
}
