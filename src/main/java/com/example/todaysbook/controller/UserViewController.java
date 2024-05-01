package com.example.todaysbook.controller;

import com.example.todaysbook.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class UserViewController {

    private final UserService userService;

    UserViewController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String registration() {
        return "user/registration";
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
}