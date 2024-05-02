package com.example.todaysbook.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecommendListDetailWithBookMarkDto {
    private Long listId;
    private String listTitle;
    private Long userId;
    private String nickname;
    private LocalDateTime date;
    private Boolean isBookmarked;
    private List<BookDto> bookList;
}
