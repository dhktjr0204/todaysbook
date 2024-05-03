package com.example.todaysbook.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeminiRecommendBookDto {
    private long id;
    private long bookId;
    private LocalDateTime date;

}
