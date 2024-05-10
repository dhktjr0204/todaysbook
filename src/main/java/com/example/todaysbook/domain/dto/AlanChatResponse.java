package com.example.todaysbook.domain.dto;

import lombok.Data;

@Data
public class AlanChatResponse {
    private String type;
    private AlanChatContent data;

    @Data
    public static class AlanChatContent {
        private String name;
        private String speak;
        private String content;
    }
}