package com.example.todaysbook.controller;

import com.example.todaysbook.domain.dto.RecommendListDetailDto;
import com.example.todaysbook.domain.dto.RecommendListDto;
import com.example.todaysbook.service.RecommendListService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@Controller
@RestController
@RequiredArgsConstructor
@RequestMapping("/recommend")
public class RecommendListController {

    private final RecommendListService recommendListService;

    @GetMapping("/{id}")
    public RecommendListDetailDto getRecommendListDetail(@PathVariable Long id, Model model){
        return recommendListService.getRecommendListDetail(id);
    }
}