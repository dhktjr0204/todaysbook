package com.example.todaysbook.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Review {

    private long id;
    private long bookId;
    private long userId;
    private String nickName;
    private String content;
    private int score;
    private LocalDateTime createTime;
    private int likeCount;
    private int dislikeCount;
    private boolean isLiked;
    private boolean isDisliked;
}
