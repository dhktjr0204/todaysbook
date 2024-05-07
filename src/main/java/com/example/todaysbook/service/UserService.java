package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.UserRequestDto;
import com.example.todaysbook.domain.entity.User;
import com.example.todaysbook.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.todaysbook.domain.entity.Role.ROLE_COMMON_BRONZE;

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
                .expire(null)
                .address(request.getAddress())
                .zipcode(request.getZipcode())
                .role(ROLE_COMMON_BRONZE.value())
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

    public void update(Long id, UserRequestDto request) {
        userRepository.updateNickNameById(id, request.getNickName());
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

}
