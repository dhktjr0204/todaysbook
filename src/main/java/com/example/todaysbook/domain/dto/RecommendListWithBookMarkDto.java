package com.example.todaysbook.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecommendListWithBookMarkDto {
    private Long listId;
    private String listTitle;
    private Long userId;
    private String nickname;
    private LocalDateTime date;
    private Boolean isBookMarked;
}
