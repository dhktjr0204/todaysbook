package com.example.todaysbook.repository;

import com.example.todaysbook.domain.entity.Cart;
import com.example.todaysbook.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}