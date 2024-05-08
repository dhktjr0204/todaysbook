package com.example.todaysbook.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AlanChatApiResponseData {
    @JsonProperty(required = false)
    private String name;

    @JsonProperty(required = false)
    private String speak;

    private String content;
}