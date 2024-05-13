package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.DailyOrderDto;
import com.example.todaysbook.domain.dto.OrderDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface OrderService {

    Page<DailyOrderDto> getDailyOrders(LocalDate date, Pageable pageable);
    OrderDetailDTO getOrderDetail(Long id);
}
