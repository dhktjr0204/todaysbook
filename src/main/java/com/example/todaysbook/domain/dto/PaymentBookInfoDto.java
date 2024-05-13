package com.example.todaysbook.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentBookInfoDto {
    private long bookId;
    private long cartBookId;
    private String bookName;
    private long quantity;
    private long price;
    private long mileage;

    // Getters and setters
}
