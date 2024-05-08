package com.example.todaysbook.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MyReview {

    private long id;
    private long bookId;
    private long userId;
    private String title;
    private String content;
    private int score;
}
