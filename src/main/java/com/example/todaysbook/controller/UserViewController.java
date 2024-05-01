package com.example.todaysbook.controller;

import com.example.todaysbook.domain.dto.UserRequestDto;
import com.example.todaysbook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.regex.Pattern;

@Controller
public class UserViewController {

    @Autowired
    private UserService userService;
    @GetMapping("/signup")
    public String registration() {
        return "user/registration";
    }

    @PostMapping("/registration")
    public String signup(@Validated UserRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/registration";
        }

        // 이메일 패턴 확인
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        if (!emailPattern.matcher(request.getEmail()).matches()) {
            bindingResult.rejectValue("email", "error.userRequest", "올바른 이메일 주소를 입력하세요.");
            return "user/registration";
        }

        // 이메일 중복 확인
        boolean isEmailAvailable = userService.isExistEmail(request.getEmail());
        if(!isEmailAvailable) {
            bindingResult.rejectValue("email", "error.userRequest", "이미 사용 중인 이메일입니다.");

            return "user/registration";
        }

        // 닉네임 중복 확인
        boolean isNickNameAvailable = userService.isExistNickName(request.getNickName());
        if (!isNickNameAvailable) {
            bindingResult.rejectValue("nickname", "error.userRequest", "이미 사용 중인 닉네임입니다.");
            return "user/registration";
        }

        // 비밀번호 패턴 확인
        Pattern passwordPattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&~]{8,}$");
        if (!passwordPattern.matcher(request.getPassword()).matches()) {
            bindingResult.rejectValue("password", "error.userRequest", "비밀번호는 영어소문자, 숫자, 특수문자를 포함하여 8자 이상이어야 합니다.");
            return "user/registration";
        }

        userService.save(request);
        return "redirect:login";
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