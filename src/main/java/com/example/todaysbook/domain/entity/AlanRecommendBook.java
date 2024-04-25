package com.example.todaysbook.domain.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlanRecommendBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long alanRecommendListId;
    private long bookId;
}
