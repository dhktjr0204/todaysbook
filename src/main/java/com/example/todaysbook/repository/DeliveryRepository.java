package com.example.todaysbook.repository;

import com.example.todaysbook.domain.entity.Delivery;
import com.example.todaysbook.domain.entity.OrderBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

}