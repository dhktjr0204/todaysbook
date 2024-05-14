package com.example.todaysbook.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Order {

    private Long bookId;
    private String title;
    private Long price;
    private Long count;
    private Long totalPrice;
}
