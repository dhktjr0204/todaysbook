package com.example.todaysbook.controller;

import com.example.todaysbook.domain.dto.CustomUserDetails;
import com.example.todaysbook.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserViewController {

    private final UserService userService;
    private final PasswordEncoder encoder;


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/";
    }

    @GetMapping("/signup")
    public String signup() {
        return "registration";
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
            return ResponseEntity.badRequest().body("이전 비밀번호가 올바르지 않습니다.");
        }

        boolean isAvailable = !(encoder.matches(newPassword, userDetails.getPassword()));

        return ResponseEntity.ok().body(Map.of("available", isAvailable));
    }

    @GetMapping("/getUserInfo")
    @ResponseBody
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("name", userDetails.getName());
        userInfo.put("address", userDetails.getAddress());
        userInfo.put("zipcode", userDetails.getZipcode());

        return ResponseEntity.ok(userInfo);
    }
}