package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.RecommendBookDto;
import com.example.todaysbook.domain.dto.SimpleReview;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import java.io.IOException;
import java.util.List;

public interface RecommendBookService {

    void GenerateRecommendBookList(List<SimpleReview> reviews) throws TasteException, IOException;

    List<RecommendBookDto> getRecommendBooks(Long bookId);
}
