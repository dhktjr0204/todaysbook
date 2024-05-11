package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.SalesDto;

import java.time.Month;
import java.time.Year;
import java.util.List;

public interface SalesService {

    List<SalesDto> getSalesByYear(Year year);
    List<SalesDto> getSalesByCategory(String year, String month);
}
