package com.example.todaysbook.controller;


import com.example.todaysbook.domain.dto.CustomUserDetails;
import com.example.todaysbook.exception.user.NotLoggedInException;
import com.example.todaysbook.service.BookMarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmark")
public class BookMarkController {
    private final BookMarkService bookMarkService;

    @PostMapping("/add")
    public ResponseEntity<?> addMark(@AuthenticationPrincipal CustomUserDetails userDetails, Long listId){

        if(userDetails == null) {
            throw new NotLoggedInException();
        }

        Long userId=userDetails.getUserId();

        bookMarkService.addMark(userId, listId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<?> cancelMark(@AuthenticationPrincipal CustomUserDetails userDetails, Long listId){

        if(userDetails == null) {
            throw new NotLoggedInException();
        }

        Long userId=userDetails.getUserId();

        bookMarkService.cancelMark(userId, listId);

        return ResponseEntity.ok().build();
    }
}
