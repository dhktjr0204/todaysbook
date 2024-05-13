package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.SalesDetailDto;
import com.example.todaysbook.domain.dto.SalesDto;
import com.example.todaysbook.repository.OrderBookRepository;
import com.example.todaysbook.repository.SalesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.time.Year;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesServiceImpl implements SalesService{

    private final SalesMapper salesMapper;
    private final OrderBookRepository orderBookRepository;

    @Override
    public List<SalesDto> getSalesByYear(Year year) {

        return salesMapper.getSalesByYear(year);
    }

    @Override
    public List<SalesDto> getSalesByCategory(String year, String month) {

        return salesMapper.getSalesByCategory(year, month);
    }

    @Override
    public Page<SalesDetailDto> getSalesByBook(String keyword, Pageable pageable) {

        return orderBookRepository.getSalesBooksByTitleOrAuthor(keyword, pageable);
    }
}
