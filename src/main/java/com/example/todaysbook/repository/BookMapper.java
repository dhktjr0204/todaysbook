package com.example.todaysbook.repository;

import com.example.todaysbook.domain.dto.PaymentBookInfoDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookMapper {

    int updateStockOfBook(Long bookId, Long quantity);
}
