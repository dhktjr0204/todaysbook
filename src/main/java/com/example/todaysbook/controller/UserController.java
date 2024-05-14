package com.example.todaysbook.controller;

import com.example.todaysbook.domain.dto.*;
import com.example.todaysbook.domain.entity.User;
import com.example.todaysbook.service.UserService;
import com.example.todaysbook.validate.UserRegisterValidator;
import com.example.todaysbook.validate.UserUpdateAddressValidator;
import com.example.todaysbook.validate.UserUpdateNicknameValidator;
import com.example.todaysbook.validate.UserUpdatePasswordValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PasswordEncoder encoder;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequestDto request, BindingResult result) {
        if(userService.isExistEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists : " + request.getEmail());
        }

        if(userService.isExistNickName(request.getNickName())) {
            return ResponseEntity.badRequest().body("Nickname already exists : " + request.getNickName());
        }

        UserRegisterValidator validator = new UserRegisterValidator();
        validator.validate(request, result);

        User user = userService.save(request);
        UserResponseDto response = user.toResponse();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /*@PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request, HttpServletResponse response,
                                   @RequestBody LoginRequestDto loginRequestDto){
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequestDto.getEmail());

        // 인증 객체 생성
        Authentication authentication
                = new UsernamePasswordAuthenticationToken(userDetails, loginRequestDto.getPassword(), new ArrayList<>());

        if(!userDetails.isEnabled()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            authenticationManager.authenticate(authentication);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // SecurityContextHolder : Authentication을 감싸는 객체
        SecurityContextHolder.getContext().setAuthentication(authentication);
        HttpSession session = request.getSession();
        session.setAttribute
                (HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                        SecurityContextHolder.getContext());
        Cookie cookie = new Cookie("JSESSIONID", session.getId());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setDomain("localhost");
        cookie.setMaxAge(30000 * 60);
        response.addCookie(cookie);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 세션 무효화
        }

        // 클라이언트에게 쿠키 삭제 요청
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setDomain("localhost");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        System.out.println("logout success");
        return ResponseEntity.ok().build();
    }*/

    @PutMapping ("/update/nickname")
    public ResponseEntity<?> updateUserNickname(@RequestBody UserRequestDto request, @AuthenticationPrincipal CustomUserDetails userDetails, BindingResult result) {
        String userNickname = userDetails.getNickname();

        if(userNickname.equals(request.getNickName())) {
            return ResponseEntity.badRequest().body("Same Nickname: " + userNickname);
        }

        UserUpdateNicknameValidator validator = new UserUpdateNicknameValidator();
        validator.validate(request, result);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.updateNickname(userDetails.getUserId(), request.getNickName());

        Authentication newAuthentication = new UsernamePasswordAuthenticationToken(userDetails, authentication.getCredentials(), authentication.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(newAuthentication);

        return ResponseEntity.ok().build();
    }

    @Transactional
    @PutMapping ("/update/addressInfo")
    public ResponseEntity<?> updateUserAddressInfo(@RequestBody UserRequestDto request, @AuthenticationPrincipal CustomUserDetails userDetails, BindingResult result) {
        UserUpdateAddressValidator validator = new UserUpdateAddressValidator();
        validator.validate(request, result);

        userService.updateAddressInfoById(userDetails.getUserId(), request.getAddress(), request.getZipcode());

        return ResponseEntity.ok().build();
    }

    @Transactional
    @PutMapping ("/update/password")
    public ResponseEntity<?> updateUserPassword(@RequestBody UserRequestDto request, @AuthenticationPrincipal CustomUserDetails userDetails, BindingResult result) {
        UserUpdatePasswordValidator validator = new UserUpdatePasswordValidator();
        validator.validate(request, result);

        String password = encoder.encode(request.getPassword());

        userService.updatePassword(userDetails.getUserId(), password);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<?> withdrawUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.withdraw(userDetails.getUserId());

        return ResponseEntity.ok().build();
    }
}
