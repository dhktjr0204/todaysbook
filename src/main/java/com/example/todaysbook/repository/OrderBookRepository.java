package com.example.todaysbook.repository;

import com.example.todaysbook.domain.dto.SalesDetailDto;
import com.example.todaysbook.domain.entity.OrderBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderBookRepository extends JpaRepository<OrderBook, Long> {

    @Query(value = "select new com.example.todaysbook.domain.dto.SalesDetailDto(" +
            "b.id, b.title, b.price, sum(ob.bookCount) as count, CAST((b.price * sum(ob.bookCount)) AS long) as sales) " +
            "from OrderBook ob " +
            "inner join Book b on b.id = ob.bookId " +
            "where b.title like %:keyword% or b.author like %:keyword% " +
            "group by b.id " +
            "order by sum(ob.bookCount) desc")
    Page<SalesDetailDto> getSalesBooksByTitleOrAuthor(String keyword, Pageable pageable);
}