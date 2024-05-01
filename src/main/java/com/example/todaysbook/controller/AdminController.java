package com.example.todaysbook.controller;

import com.example.todaysbook.domain.dto.AdminUserDto;
import com.example.todaysbook.domain.dto.BookDto;
import com.example.todaysbook.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final int VISIBLE_PAGE=5;

    @GetMapping("/userlist")
    public String allUserList(@PageableDefault(page = 0, size = 5, sort="id", direction = Sort.Direction.ASC) Pageable pageable,
                           Model model){
        Page<AdminUserDto> allUserList = adminService.findAllUser(pageable);

        HashMap<String,Integer> pages=calculatePage(allUserList.getPageable().getPageNumber(), allUserList.getTotalPages());
        int startPage=pages.get("startPage");
        int endPage=pages.get("endPage");

        model.addAttribute("users", allUserList);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage", endPage);

        return "admin/userlist";
    }

    @GetMapping("/stocklist")
    public String allStockList(@PageableDefault(page = 0, size = 5, sort="id", direction = Sort.Direction.ASC) Pageable pageable,
                               Model model){
        Page<BookDto> allBook = adminService.findAllBook(pageable);

        HashMap<String,Integer> pages=calculatePage(allBook.getPageable().getPageNumber(), allBook.getTotalPages());
        int startPage=pages.get("startPage");
        int endPage=pages.get("endPage");

        model.addAttribute("books", allBook);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage", endPage);

        return "admin/stocklist";
    }


    @GetMapping("/userlist/search")
    public String searchUserList(@PageableDefault(page = 0, size = 5, sort="id", direction = Sort.Direction.ASC) Pageable pageable,
                                 @RequestParam(value = "keyword") String keyword, Model model){
        Page<AdminUserDto> usersByKeyword = adminService.findUsersByKeyword(keyword, pageable);

        int startPage=0;
        int endPage=0;

        if(!usersByKeyword.isEmpty()) {
            HashMap<String, Integer> pages = calculatePage(usersByKeyword.getPageable().getPageNumber(), usersByKeyword.getTotalPages());
            startPage = pages.get("startPage");
            endPage = pages.get("endPage");
        }

        model.addAttribute("users", usersByKeyword);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("keyword", keyword);

        return "admin/userlist";
    }

    @GetMapping("/stocklist/search")
    public String searchBookList(@PageableDefault(page = 0, size = 5, sort="id", direction = Sort.Direction.ASC) Pageable pageable,
                                 @RequestParam(value = "keyword") String keyword, Model model){
        Page<BookDto> booksByKeyword = adminService.findBooksByKeyword(keyword, pageable);

        int startPage=0;
        int endPage=0;

        if(!booksByKeyword.isEmpty()) {
            HashMap<String, Integer> pages = calculatePage(booksByKeyword.getPageable().getPageNumber(), booksByKeyword.getTotalPages());
            startPage = pages.get("startPage");
            endPage = pages.get("endPage");
        }

        model.addAttribute("books", booksByKeyword);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("keyword", keyword);

        return "admin/stocklist";
    }

    private HashMap<String, Integer> calculatePage(int currentPage, int totalPage){
        HashMap<String, Integer> result= new HashMap<>();

        int startPage=(currentPage/VISIBLE_PAGE)*VISIBLE_PAGE+1;
        int endPage=Math.min(totalPage, (currentPage/VISIBLE_PAGE)*VISIBLE_PAGE+VISIBLE_PAGE);

        result.put("startPage", startPage);
        result.put("endPage", endPage);

        return result;
    }
}
