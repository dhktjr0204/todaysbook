package com.example.todaysbook.repository;

import com.example.todaysbook.domain.dto.SalesDetailDto;
import com.example.todaysbook.domain.dto.SalesDto;
import org.apache.ibatis.annotations.Mapper;

import java.time.Year;
import java.util.List;

@Mapper
public interface SalesMapper {

    List<SalesDto> getSalesByYear(Year year);
    List<SalesDto> getSalesByCategory(String year, String month);
}