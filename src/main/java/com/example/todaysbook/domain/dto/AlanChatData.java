package com.example.todaysbook.domain.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlanChatData {
    private String content;
    private String name;
    private String speak;
}