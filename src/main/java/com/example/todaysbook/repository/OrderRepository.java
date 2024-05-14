package com.example.todaysbook.repository;

import com.example.todaysbook.domain.dto.DailyOrderDto;
import com.example.todaysbook.domain.dto.MyPageOrderDto;
import com.example.todaysbook.domain.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {

    @Query(value = "select new com.example.todaysbook.domain.dto.DailyOrderDto(" +
            "o.id, o.orderDate, sum(ob.bookCount * b.price) as sales) " +
            "from Orders o " +
            "inner join OrderBook ob on ob.orderId = o.id " +
            "inner join Book b on b.id = ob.bookId " +
            "where date(o.orderDate) = :date " +
            "group by o.id " +
            "order by o.id")
    Page<DailyOrderDto> getOrdersByOrderDate(LocalDate date, Pageable pageable);
    long count();

    @Query(value = "select new com.example.todaysbook.domain.dto.MyPageOrderDto(" +
            "o.id, o.deliveryId, o.orderDate, sum(ob.bookCount * b.price) as sales) " +
            "from Orders o " +
            "inner join OrderBook ob on ob.orderId = o.id " +
            "inner join Book b on b.id = ob.bookId " +
            "where o.userId = :userId " +
            "group by o.id " +
            "order by o.orderDate desc")
    Page<MyPageOrderDto> getOrdersByUserId(Long userId, Pageable pageable);
}