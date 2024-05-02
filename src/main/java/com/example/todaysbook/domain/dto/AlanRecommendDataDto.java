package com.example.todaysbook.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlanRecommendDataDto {

    private long id;

    private String title;

    private LocalDateTime createdAt;

}