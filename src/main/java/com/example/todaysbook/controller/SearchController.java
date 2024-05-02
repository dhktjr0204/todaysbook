package com.example.todaysbook.controller;

import com.example.todaysbook.domain.dto.BookDto;
import com.example.todaysbook.domain.dto.RecommendListDetailWithBookMarkDto;
import com.example.todaysbook.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/book")
public class SearchController {
    private final SearchService searchService;
    private final int VISIBLE_PAGE = 5;

    @GetMapping("/search")
    public String searchBook(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                             @RequestParam(value = "keyword") String keyword, Model model) {
        Page<BookDto> result = searchService.searchBookByKeyword(keyword, pageable);

        int startPage = 0;
        int endPage = 0;

        if (!result.isEmpty()) {
            HashMap<String, Integer> pages = calculatePage(result.getPageable().getPageNumber(), result.getTotalPages());
            startPage = pages.get("startPage");
            endPage = pages.get("endPage");
        }

        model.addAttribute("books", result);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("keyword", keyword);

        return "book/search";
    }

    @GetMapping("/search/list")
    public String searchLists(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                              @RequestParam(value = "keyword") String keyword, Model model) {
        long userId = 1;

        Page<RecommendListDetailWithBookMarkDto> result = searchService.searchListByKeyword(keyword, userId, pageable);

        int startPage = 0;
        int endPage = 0;

        if (!result.isEmpty()) {
            HashMap<String, Integer> pages = calculatePage(result.getPageable().getPageNumber(), result.getTotalPages());
            startPage = pages.get("startPage");
            endPage = pages.get("endPage");
        }

        model.addAttribute("recommendLists", result);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("keyword", keyword);

        return "book/search_list";
    }

    @GetMapping("/search/create/list")
    public ResponseEntity<Page<BookDto>> searchBooks(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                                                     @RequestParam(value = "keyword") String keyword, Model model) {
        return ResponseEntity.ok(searchService.searchBookByKeyword(keyword, pageable));
    }

    private HashMap<String, Integer> calculatePage(int currentPage, int totalPage) {
        HashMap<String, Integer> result = new HashMap<>();

        int startPage = (currentPage / VISIBLE_PAGE) * VISIBLE_PAGE + 1;
        int endPage = Math.min(totalPage, (currentPage / VISIBLE_PAGE) * VISIBLE_PAGE + VISIBLE_PAGE);

        result.put("startPage", startPage);
        result.put("endPage", endPage);

        return result;
    }
}
