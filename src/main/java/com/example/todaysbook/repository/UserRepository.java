package com.example.todaysbook.repository;

import com.example.todaysbook.domain.entity.User;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.email = :email")
    boolean isExistEmail(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.nickName = :nickName")
    boolean isExistNickName(@Param("nickName") String nickName);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.nickName = :nickName WHERE u.id = :id")
    void updateNickname(@Param("id") Long id, @Param("nickName") String nickName);

    @Modifying
    @Query("UPDATE User u SET u.address = :address, u.zipcode = :zipcode WHERE u.id = :id")
    void updateAddressInfoById(@Param("id") Long id, @Param("address") String address, @Param("zipcode") String zipcode);

    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.id = :id")
    void updatePassword(@Param("id") Long id, @Param("password") String password);

    /*@Query("UPDATE User u SET u.expire = CURRENT_DATE, u.is_expired = FALSE WHERE u.id = :id")
    void withdrawById(@Param("id") Long id);*/

    User findById(long id);
    Page<User> findAll(Pageable pageable);
    Page<User> findByEmailContainingOrNickNameContaining(String email, String nickName, Pageable pageable);

}