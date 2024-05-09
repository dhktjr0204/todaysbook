package com.example.todaysbook.domain.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlanChatResponse {
    private String type;
    private AlanChatData data;
    private String name;
}