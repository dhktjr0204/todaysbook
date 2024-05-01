package com.example.todaysbook.controller;

import com.example.todaysbook.domain.dto.BookDetailDto;
import com.example.todaysbook.service.BookDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class BookDetailController {

    private final BookDetailService bookDetailService;

    @RequestMapping("/book/detail/{bookId}")
    public String getBookDetail(@PathVariable Long bookId, Model model) {

        long userId = 1l;

        BookDetailDto bookDetailDto = bookDetailService.getBookDetail(bookId, userId);

        model.addAttribute("bookDetailDto", bookDetailDto);

        return "book/detail";
    }
}
