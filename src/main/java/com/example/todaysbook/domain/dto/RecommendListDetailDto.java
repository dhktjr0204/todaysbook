package com.example.todaysbook.domain.dto;

import com.example.todaysbook.domain.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecommendListDetailDto {
    private Long listId;
    private String listTitle;
    private Long userId;
    private String nickname;
    private Date date;
    private List<BookDto> bookList;
}
