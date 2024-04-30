package com.example.todaysbook.domain.entity;

import com.example.todaysbook.domain.dto.AlanRecommendBookDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlanRecommendBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public AlanRecommendBookDto toDto() {
        return AlanRecommendBookDto.builder()
                .id(id)
                .title(title)
                .price(price)
                .author(author)
                .publisher(publisher)
                .publishDate(publishDate)
                .stock(stock)
                .isbn(isbn)
                .description(description)
                .imagePath(imagePath)
                .build();
    }
}