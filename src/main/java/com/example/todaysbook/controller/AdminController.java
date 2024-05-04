package com.example.todaysbook.controller;

import com.example.todaysbook.domain.dto.AdminUserDto;
import com.example.todaysbook.domain.dto.BookDto;
import com.example.todaysbook.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final int VISIBLE_PAGE=5;

    //유저 관리
    @GetMapping("/userlist")
    public String allUserList(@PageableDefault(page = 0, size = 5, sort="id", direction = Sort.Direction.ASC) Pageable pageable,
                           Model model){
        Page<AdminUserDto> allUserList = adminService.findAllUser(pageable);

        HashMap<String,Integer> pages=calculatePage(allUserList.getPageable().getPageNumber(), allUserList.getTotalPages());
        int startPage=pages.get("startPage");
        int endPage=pages.get("endPage");

        model.addAttribute("dto", allUserList);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage", endPage);

        return "admin/userlist";
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

        model.addAttribute("dto", usersByKeyword);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("keyword", keyword);

        return "admin/userlist";
    }

    @PutMapping("/userlist")
    public ResponseEntity<?> updateUserRole(Long userId, String role){
        adminService.updateUserRole(userId, role);

        return ResponseEntity.ok("수정되었습니다.");
    }

    @DeleteMapping("/userlist")
    public ResponseEntity<?> deleteUser(Long userId){
        adminService.deleteUser(userId);

        return ResponseEntity.ok("삭제되었습니다.");
    }

    //수량 관리
    @GetMapping("/stocklist")
    public String allStockList(@PageableDefault(page = 0, size = 5, sort="id", direction = Sort.Direction.ASC) Pageable pageable,
                               Model model){
        Page<BookDto> allBook = adminService.findAllBook(pageable);

        HashMap<String,Integer> pages=calculatePage(allBook.getPageable().getPageNumber(), allBook.getTotalPages());
        int startPage=pages.get("startPage");
        int endPage=pages.get("endPage");

        model.addAttribute("dto", allBook);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage", endPage);

        return "admin/stocklist";
    }

    @GetMapping("/stocklist/search")
    public String searchStockList(@PageableDefault(page = 0, size = 5, sort="id", direction = Sort.Direction.ASC) Pageable pageable,
                                  @RequestParam(value = "keyword") String keyword, Model model){
        Page<BookDto> booksByKeyword = adminService.findBooksByKeyword(keyword, pageable);

        int startPage=0;
        int endPage=0;

        if(!booksByKeyword.isEmpty()) {
            HashMap<String, Integer> pages = calculatePage(booksByKeyword.getPageable().getPageNumber(), booksByKeyword.getTotalPages());
            startPage = pages.get("startPage");
            endPage = pages.get("endPage");
        }

        model.addAttribute("dto", booksByKeyword);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("keyword", keyword);

        return "admin/stocklist";
    }

    @PutMapping("/stocklist")
    public ResponseEntity<?> updateBookStock(Long bookId, Long stock){
        adminService.updateBookStock(bookId, stock);

        return ResponseEntity.ok("수정되었습니다.");
    }

    @DeleteMapping("/stocklist")
    public ResponseEntity<?> deleteBook(Long bookId){
        adminService.deleteBook(bookId);

        return ResponseEntity.ok("삭제되었습니다");
    }


    //책 관리
    @GetMapping("/booklist")
    public String allBooklist(@PageableDefault(page = 0, size = 5, sort="id", direction = Sort.Direction.ASC) Pageable pageable,
                               Model model){
        Page<BookDto> allBook = adminService.findAllBook(pageable);

        HashMap<String,Integer> pages=calculatePage(allBook.getPageable().getPageNumber(), allBook.getTotalPages());
        int startPage=pages.get("startPage");
        int endPage=pages.get("endPage");

        model.addAttribute("dto", allBook);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage", endPage);

        return "admin/booklist";
    }

    @GetMapping("/booklist/search")
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

        model.addAttribute("dto", booksByKeyword);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("keyword", keyword);

        return "admin/booklist";
    }

    @GetMapping("/booklist/edit")
    public String getBookEditForm(Long bookId, Model model){

        BookDto book = adminService.findBookById(bookId);

        model.addAttribute("book",book);

        return "admin/book-edit-form";
    }

    @PutMapping("/booklist/edit")
    public ResponseEntity<?> updateBook(BookDto bookDto){

        adminService.updateBook(bookDto);

        return ResponseEntity.ok("수정되었습니다.");
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
