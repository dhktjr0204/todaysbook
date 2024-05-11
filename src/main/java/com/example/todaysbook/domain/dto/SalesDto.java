package com.example.todaysbook.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Month;
import java.time.Year;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SalesDto {

    private Month month;
    private String categoryName;
    private Long sales;
}
