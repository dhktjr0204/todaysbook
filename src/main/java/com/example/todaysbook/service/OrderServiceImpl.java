package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.DailyOrderDto;
import com.example.todaysbook.domain.dto.DeliveryDto;
import com.example.todaysbook.domain.dto.MyPageOrderDto;
import com.example.todaysbook.domain.dto.Order;
import com.example.todaysbook.domain.dto.OrderDetailDTO;
import com.example.todaysbook.domain.dto.OrderInfo;
import com.example.todaysbook.domain.entity.Delivery;
import com.example.todaysbook.domain.entity.Orders;
import com.example.todaysbook.exception.delivery.DeliveryNotFoundException;
import com.example.todaysbook.exception.order.OrderNotFoundException;
import com.example.todaysbook.repository.DeliveryRepository;
import com.example.todaysbook.repository.OrderMapper;
import com.example.todaysbook.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final DeliveryRepository deliveryRepository;

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

    @Override
    public Orders getOrders(Long id) {
        return orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
    }


    @Override
    public Page<DeliveryDto> getDelivery(Pageable pageable) {
        return deliveryRepository.findAllDelivery(pageable);
    }

    @Override
    public DeliveryDto getDeliveryDetail(String id) {
        return deliveryRepository.findByIdWithUser(id).orElseThrow(DeliveryNotFoundException::new);
    }

    @Transactional
    @Override
    public void updateDeliveryStatus(String deliveryId, String status) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(DeliveryNotFoundException::new);

        delivery.updateStatus(status);
    }

    @Override
    public Page<DeliveryDto> getDeliveryByKeyword(String keyword, Pageable pageable) {
        return deliveryRepository.findDeliveryByKeyword(keyword, pageable);
    }

    @Override
    public Page<MyPageOrderDto> getMyOrders(Long userId, Pageable pageable) {
        return orderRepository.getOrdersByUserId(userId, pageable);
    }


}