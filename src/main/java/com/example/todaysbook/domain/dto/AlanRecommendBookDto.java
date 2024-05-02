package com.example.todaysbook.domain.dto;

import com.example.todaysbook.domain.entity.AlanRecommendBook;
import com.example.todaysbook.domain.entity.Book;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlanRecommendBookDto {

    private long id;
    private String title;
    private long price;
    private String author;
    private String publisher;
    private Timestamp publishDate;
    private long stock;
    private String isbn;
    private String description;
    private String imagePath;

}