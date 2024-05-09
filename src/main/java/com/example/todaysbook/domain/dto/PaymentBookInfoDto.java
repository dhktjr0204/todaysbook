package com.example.todaysbook.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentBookInfoDto {
    private String bookName;
    private int quantity;
    private int price;
    private String mileage;

    // Getters and setters
}
