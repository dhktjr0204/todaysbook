package com.example.todaysbook.controller;

import com.example.todaysbook.domain.dto.RecommendListCreateRequestDto;
import com.example.todaysbook.domain.dto.RecommendListUpdateRequestDto;
import com.example.todaysbook.domain.dto.RecommendListDetailDto;
import com.example.todaysbook.domain.entity.UserRecommendList;
import com.example.todaysbook.service.RecommendListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/add")
    public ResponseEntity<String> createRecommnedList(RecommendListCreateRequestDto request){

        UserRecommendList save = recommendListService.save(request);

        return ResponseEntity.ok("/");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateRecommendList(RecommendListUpdateRequestDto request){

        recommendListService.update(request);

        return ResponseEntity.ok("/");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> deleteRecommendList(Long listId){

        recommendListService.delete(listId);

        return ResponseEntity.ok("/");
    }

}