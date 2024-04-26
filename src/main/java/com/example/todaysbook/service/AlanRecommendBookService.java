package com.example.todaysbook.service;

import com.example.todaysbook.repository.AlanRecommendBookRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AlanRecommendBookService {

    private final RestTemplate restTemplate;
    private final AlanRecommendBookRepository alanRecommendBookRepository;

    public AlanRecommendBookService(RestTemplate restTemplate, AlanRecommendBookRepository alanRecommendBookRepository) {
        this.restTemplate = restTemplate;
        this.alanRecommendBookRepository = alanRecommendBookRepository;
    }




}
