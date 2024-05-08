package com.example.todaysbook.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AlanChatApiResponseWrapper { // API 응답을 래핑하는 용도
    private String type;
    private AlanChatApiResponseData data;

}