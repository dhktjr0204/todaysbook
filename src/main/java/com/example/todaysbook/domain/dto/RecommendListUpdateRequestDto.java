package com.example.todaysbook.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecommendListUpdateRequestDto {
    private Long userId;
    private String title;
    private List<Long> newBookList;
    private List<Long> deleteBookList;
}
