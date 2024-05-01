package com.example.todaysbook.controller;

import com.example.todaysbook.domain.dto.UserRequestDto;
import com.example.todaysbook.domain.dto.UserResponseDto;
import com.example.todaysbook.domain.entity.User;
import com.example.todaysbook.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public UserController (UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequestDto request) {
        if(userService.isExistEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists : " + request.getEmail());
        }

        if(userService.isExistNickName(request.getNickName())) {
            return ResponseEntity.badRequest().body("Nickname already exists : " + request.getNickName());
        }

        User user = userService.save(request);
        UserResponseDto response = user.toResponse();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequestDto request) {
        // 사용자 인증을 시도
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // 인증 정보를 SecurityContextHolder에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 로그인 성공 시 빈 응답 반환
        return ResponseEntity.ok().build();
    }


    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping ("/update/{id}")
    public ResponseEntity<?> updateUser(@RequestBody UserRequestDto request, @PathVariable Long id) {
        if(userService.isExistNickName(request.getNickName())) {
            return ResponseEntity.badRequest().body("Nickname already exists : " + request.getNickName());
        }

        if(userService.findById(id).isEmpty()) {
            return ResponseEntity.badRequest().body("id doesn't exists : " +  id);
        }

        userService.update(id, request);

        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/withdraw/{id}")
    public ResponseEntity<?> withdrawUser(@PathVariable Long id) {
        userService.withdraw(id);

        return ResponseEntity.ok().build();
    }

}
