package com.example.todaysbook.domain.dto;

import com.example.todaysbook.domain.entity.Book;
import com.example.todaysbook.domain.entity.Cart;
import lombok.*;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartRequestDto {
    private long userId;
    private long cartId;
    private long bookId;
    private long count;
    private String author;
    private String publisher;
    private long price;
    private String title;
    private long totalPrice;
    private Book book;

}

