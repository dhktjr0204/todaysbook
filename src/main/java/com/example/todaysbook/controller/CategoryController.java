package com.example.todaysbook.controller;


import com.example.todaysbook.domain.CategoryEnum;
import com.example.todaysbook.domain.dto.BookDto;
import com.example.todaysbook.service.CategoryService;
import com.example.todaysbook.util.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

@Controller
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/{id}")
    public String getCategoryBooks(@PathVariable("id") String categoryId,
                                   @RequestParam(defaultValue = "title") String type,
                                   @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                                   Model model) {

        Page<BookDto> books=selectBookSort(type, categoryId, pageable);

        int startPage = 0;
        int endPage = 0;

        if (!books.isEmpty()) {
            HashMap<String, Integer> pages = Pagination.calculatePage(books.getPageable().getPageNumber(), books.getTotalPages());
            startPage = pages.get("startPage");
            endPage = pages.get("endPage");
        }

        String categoryName = convertNameToId(categoryId);

        model.addAttribute("dto", books);
        model.addAttribute("categoryName", categoryName);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("type", type);

        return "book/categoryBook";
    }

    private Page<BookDto> selectBookSort(String type, String categoryId, Pageable pageable) {
        if (type.equals("bestseller")) {
            return categoryService.getBooksSortByBestSeller(categoryId, pageable);
        }

        if (type.equals("review")) {
            return categoryService.getBooksSortByReviewScore(categoryId, pageable);
        }

        return categoryService.getBooksByCategoryId(categoryId, pageable);
    }

    private String convertNameToId(String categoryId){
        for (CategoryEnum categoryEnum : CategoryEnum.values()) {
            if (categoryEnum.getKey().equals(categoryId)) {
                return categoryEnum.getTitle();
            }
        }
        return null;
    }
}
