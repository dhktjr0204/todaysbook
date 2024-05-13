package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.PaymentAddressAndMileageInfo;
import com.example.todaysbook.domain.dto.PaymentBookInfoDto;
import com.example.todaysbook.domain.entity.Delivery;
import com.example.todaysbook.domain.entity.OrderBook;
import com.example.todaysbook.domain.entity.Orders;
import com.example.todaysbook.domain.entity.User;
import com.example.todaysbook.repository.DeliveryRepository;
import com.example.todaysbook.repository.OrderBookRepository;
import com.example.todaysbook.repository.OrderRepository;
import com.example.todaysbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final OrderBookRepository orderBookRepository;
    private final DeliveryRepository deliveryRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void createOrder(long userId, List<PaymentBookInfoDto> bookDtoList, PaymentAddressAndMileageInfo addressAndMileageInfo) {

        Delivery delivery = deliveryRepository.save(Delivery.builder().status("배송중").address(addressAndMileageInfo.getAddress() + "," + addressAndMileageInfo.getDetailAddress())
                .zipcode(addressAndMileageInfo.getPostcode()).build());

        Orders order = orderRepository.save(Orders.builder().userId(userId).status("완료").deliveryId(delivery.getId()).build());

        AtomicLong plusMileage = new AtomicLong();
        bookDtoList.forEach(paymentBookInfoDto -> {
            plusMileage.addAndGet(paymentBookInfoDto.getMileage());
            orderBookRepository.save(OrderBook.builder().bookId(paymentBookInfoDto.getBookId()).orderId(order.getId()).bookCount(paymentBookInfoDto.getQuantity()).build());
        });

        User byId = userRepository.findById(userId);
        byId.updateMileage(-addressAndMileageInfo.getUsedMileage());
        byId.updateMileage(plusMileage.get());

        List<Long> collect = bookDtoList.stream().map(paymentBookInfoDto -> paymentBookInfoDto.getCartBookId()).collect(Collectors.toList());
        cartService.deleteSelectedCartItems(collect);
    }
}
