package com.example.todaysbook.controller;


import com.example.todaysbook.service.BookMarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> addMark(Long listId){
        long userId= 1;

        bookMarkService.addMark(userId, listId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<?> cancelMark(Long listId){
        long userId=1;

        bookMarkService.cancelMark(userId, listId);

        return ResponseEntity.ok().build();
    }
}
