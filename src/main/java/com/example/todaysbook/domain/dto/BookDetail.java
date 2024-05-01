package com.example.todaysbook.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookDetail {

    private Long id;
    private String title;
    private String author;
    private long price;
    private String image;
    private String publisher;
    private LocalDateTime publishDate;
    private long stock;
    private String isbn;
    private String description;
    private String imagePath;
    private boolean isFavorite;
    private long bronzeMileage;
    private long silverMileage;
    private long goldMileage;
    private long diamondMileage;
    private double score;
    private int starScore;
}
