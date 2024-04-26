package com.example.todaysbook.controller;

import com.example.todaysbook.domain.dto.SearchResponseDto;
import com.example.todaysbook.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/book")
public class SearchController {
    private final SearchService searchService;
    private final int VISIBLE_PAGE=5;

    @GetMapping("/search")
    public String search(@PageableDefault(page=0, size=10, sort="id", direction = Sort.Direction.ASC)Pageable pageable,
                         @RequestParam(value = "keyword") String keyword, Model model){
        Page<SearchResponseDto> result = searchService.searchByKeyword(keyword, pageable);

        int startPage=0;
        int endPage=0;

        if(!result.isEmpty()){
            int currentPage=result.getPageable().getPageNumber();
            startPage=(currentPage/VISIBLE_PAGE)*VISIBLE_PAGE+1;
            endPage=Math.min(result.getTotalPages(), (currentPage/VISIBLE_PAGE)*VISIBLE_PAGE+VISIBLE_PAGE);
        }

        model.addAttribute("books", result);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("keyword",keyword);

        return "book/search";
    }
}
