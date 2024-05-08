package com.example.todaysbook.repository;

import com.example.todaysbook.domain.entity.AlanChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlanChatHistoryRepository extends JpaRepository<AlanChatHistory, Long> {


}