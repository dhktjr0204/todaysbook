package com.example.todaysbook.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private long price;
    private String author;
    private String publisher;
    private LocalDateTime publishDate;
    private long stock;
    private String isbn;
    private String description;
    private String imagePath;

    public void updateStock(Long stock){
        this.stock=stock;
    }
    public void updateBook(String title, Long price, String author, String publisher, Long stock, String description){
        this.title=title;
        this.price=price;
        this.author=author;
        this.publisher=publisher;
        this.stock=stock;
        this.description=description;
    }
}
