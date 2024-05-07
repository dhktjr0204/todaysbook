package com.example.todaysbook.controller;

import com.example.todaysbook.domain.dto.ReviewRequestDto;
import com.example.todaysbook.domain.dto.SimpleReview;
import com.example.todaysbook.service.RecommendBookService;
import com.example.todaysbook.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recommend_book")
public class RecommendBookController {

    private final RecommendBookService recommendBookService;
    private final ReviewService reviewService;

    @GetMapping("")
    public void getRecommendBook() throws IOException, TasteException {

        List<SimpleReview> reviews = reviewService.getSimpleReviews();
        recommendBookService.GenerateRecommendBookList(reviews);
    }
}
