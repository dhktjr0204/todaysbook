package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.Order;
import com.example.todaysbook.domain.dto.OrderDetailDTO;
import com.example.todaysbook.domain.dto.OrderInfo;
import com.example.todaysbook.repository.OrderMapper;
import com.example.todaysbook.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;
    @Mock
    private OrderMapper orderMapper;

    @Test
    @DisplayName("주문 정보 상세 테스트")
    public void getOrderDetailTest() {
        // given
        long orderId = 1L;
        OrderInfo orderInfo = OrderInfo.builder()
                .id(orderId)
                .orderDate(LocalDateTime.now())
                .userName("testUser")
                .address("testAddress")
                .build();
        List<Order> orders = Arrays.asList(
                Order.builder().bookId(1L).title("Book 1").price(10000L).count(2L).totalPrice(20000L).build(),
                Order.builder().bookId(2L).title("Book 2").price(15000L).count(1L).totalPrice(15000L).build()
        );
        long totalPrice = orders.stream().mapToLong(Order::getTotalPrice).sum();

        when(orderMapper.getOrderInfo(anyLong())).thenReturn(orderInfo);
        when(orderMapper.getOrders(anyLong())).thenReturn(orders);
        when(orderMapper.getTotalPrice(anyLong())).thenReturn(totalPrice);

        // when
        OrderDetailDTO orderDetailDTO = orderService.getOrderDetail(orderId);

        // then
        assertEquals(orderInfo, orderDetailDTO.getOrderInfo());
        assertEquals(orders, orderDetailDTO.getOrders());
        assertEquals(totalPrice, orderDetailDTO.getTotalPrice());
    }
}
