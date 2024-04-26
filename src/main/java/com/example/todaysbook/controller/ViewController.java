package com.example.todaysbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/index")
    public String index(Model model) {

        return "index";
    }

    @GetMapping("/search")
    public String search(Model model) {

        return "book/search";
    }

    @GetMapping("/detail")
    public String detail(Model model) {

        return "book/detail";
    }
}
