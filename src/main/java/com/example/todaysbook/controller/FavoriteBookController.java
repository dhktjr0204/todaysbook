package com.example.todaysbook.controller;


import com.example.todaysbook.domain.dto.CustomUserDetails;
import com.example.todaysbook.service.FavoriteBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/favorite_book")
public class FavoriteBookController {

    private final FavoriteBookService favoriteBookService;

    @PostMapping("/add")
    public ResponseEntity<?> addFavoriteBook(@RequestParam(value = "bookId") long bookId,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {

        try {

            long userId = userDetails.getUserId();

            return ResponseEntity.ok(favoriteBookService.addFavoriteBook(userId, bookId));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFavoriteBook(@RequestParam(value = "bookId") long bookId,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {

        try {

            long userId = userDetails.getUserId();

            return ResponseEntity.ok(favoriteBookService.deleteFavoriteBook(userId, bookId));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
        }
    }
}
