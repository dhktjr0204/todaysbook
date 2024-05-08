package com.example.todaysbook.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AlanChatApiResponseData {
    // AlanChatApiResponseWrapper의 data 필드에 들어갈 데이터를 정의
    private String content;
}