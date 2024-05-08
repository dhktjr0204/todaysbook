package com.example.todaysbook.controller;

import com.example.todaysbook.domain.dto.CustomUserDetails;
import com.example.todaysbook.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class UserViewController {

    private final UserService userService;
    private final PasswordEncoder encoder;

            UserViewController(UserService userService, PasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

    @GetMapping("/checkEmailAvailability")
    @ResponseBody
    public ResponseEntity<?> checkEmailAvailability(@RequestParam String email) {
        boolean isAvailable = !(userService.isExistEmail(email));
        return ResponseEntity.ok().body(Map.of("available", isAvailable));
    }

    @GetMapping("/checkNicknameAvailability")
    @ResponseBody
    public ResponseEntity<?> checkNicknameAvailability(@RequestParam String nickname) {
        boolean isAvailable = !(userService.isExistNickName(nickname));
        return ResponseEntity.ok().body(Map.of("available", isAvailable));
    }

    @GetMapping("/checkPasswordAvailability")
    @ResponseBody
    public ResponseEntity<?> checkPasswordAvailability(@RequestParam String originPassword,@RequestParam String newPassword, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if(userDetails == null) {
            System.out.println("userDetails is null");
            return null;
        }

        if(!encoder.matches(originPassword, userDetails.getPassword())) {
            return ResponseEntity.badRequest().body("Entered Origin Password is wrong");
        }

        boolean isAvailable = !(encoder.matches(newPassword, userDetails.getPassword()));

        return ResponseEntity.ok().body(Map.of("available", isAvailable));
    }
}