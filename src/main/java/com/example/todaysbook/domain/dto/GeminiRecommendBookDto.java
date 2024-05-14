package com.example.todaysbook.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class GeminiRecommendBookDto {
    private long id;
    private BookDto bookDto;
}