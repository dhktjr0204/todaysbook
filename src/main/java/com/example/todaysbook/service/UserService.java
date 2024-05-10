package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.UserRequestDto;
import com.example.todaysbook.domain.entity.User;
import com.example.todaysbook.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public User save(UserRequestDto request) {
        if(request == null) {
            System.out.println("request is null");
            return null;
        }

        User user = User.builder()
                .name(request.getName())
                .nickName(request.getNickName())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .mileage("0")
                .address(request.getAddress())
                .zipcode(request.getZipcode())
                .role("ROLE_BRONZE")
                .build();

        return userRepository.save(user);
    }

    public void withdraw(Long id) {
        userRepository.deleteById(id);
    }

    public boolean isExistEmail(String email) {
        return userRepository.isExistEmail(email);
    }

    public boolean isExistNickName(String nickName) {
        return userRepository.isExistNickName(nickName);
    }

    public void updateNickname(Long id, String nickname) {
        userRepository.updateNickname(id, nickname);
    }

    public void updatePassword(Long id, String password) {
        userRepository.updatePassword(id, password);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public void updateAddressInfoById(Long id, String address, String zipcode) {
        userRepository.updateAddressInfoById(id, address, zipcode);
    }
}
