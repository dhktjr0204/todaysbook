package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.DailyOrderDto;
import com.example.todaysbook.domain.dto.Order;
import com.example.todaysbook.domain.dto.OrderDetailDTO;
import com.example.todaysbook.domain.dto.OrderInfo;
import com.example.todaysbook.repository.OrderMapper;
import com.example.todaysbook.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public Page<DailyOrderDto> getDailyOrders(LocalDate date, Pageable pageable) {

        return orderRepository.getOrdersByOrderDate(date, pageable);
    }

    @Override
    public OrderDetailDTO getOrderDetail(Long id) {

        OrderInfo orderInfo = orderMapper.getOrderInfo(id);
        List<Order> orders = orderMapper.getOrders(id);
        Long totalPrice = orderMapper.getTotalPrice(id);

        return new OrderDetailDTO().builder()
                .orderInfo(orderInfo)
                .orders(orders)
                .totalPrice(totalPrice)
                .build();
    }
}