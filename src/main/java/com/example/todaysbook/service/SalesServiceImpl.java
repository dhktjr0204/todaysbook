package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.SalesDto;
import com.example.todaysbook.repository.SalesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.time.Year;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesServiceImpl implements SalesService{

    private final SalesMapper salesMapper;

    @Override
    public List<SalesDto> getSalesByYear(Year year) {

        return salesMapper.getSalesByYear(year);
    }

    @Override
    public List<SalesDto> getSalesByCategory(String year, String month) {

        return salesMapper.getSalesByCategory(year, month);
    }
}
