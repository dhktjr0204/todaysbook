package com.example.todaysbook.repository;

import com.example.todaysbook.domain.dto.Order;
import com.example.todaysbook.domain.dto.OrderInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {

    OrderInfo getOrderInfo(Long id);
    List<Order> getOrders(Long id);
    Long getTotalPrice(Long id);
}
