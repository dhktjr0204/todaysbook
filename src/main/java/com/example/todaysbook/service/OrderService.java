package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.DailyOrderDto;
import com.example.todaysbook.domain.dto.DeliveryDto;
import com.example.todaysbook.domain.dto.MyPageOrderDto;
import com.example.todaysbook.domain.dto.OrderDetailDTO;
import com.example.todaysbook.domain.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface OrderService {

    Page<DailyOrderDto> getDailyOrders(LocalDate date, Pageable pageable);
    OrderDetailDTO getOrderDetail(Long id);
    Orders getOrders(Long id);
    Page<DeliveryDto> getDelivery(Pageable pageable);
    DeliveryDto getDeliveryDetail(String id);
    void updateDeliveryStatus(String deliveryId, String status);
    Page<DeliveryDto> getDeliveryByKeyword(String keyword, Pageable pageable);
    Page<MyPageOrderDto> getMyOrders(Long userId, Pageable pageable);
}
