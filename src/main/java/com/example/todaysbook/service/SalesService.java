package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.SalesDetailDto;
import com.example.todaysbook.domain.dto.SalesDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Month;
import java.time.Year;
import java.util.List;

public interface SalesService {

    List<SalesDto> getSalesByYear(Year year);
    List<SalesDto> getSalesByCategory(String year, String month);
    Page<SalesDetailDto> getSalesByBook(String keyword, Pageable pageable);
}
