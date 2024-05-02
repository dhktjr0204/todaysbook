package com.example.todaysbook.repository;

import com.example.todaysbook.domain.entity.Book;
import com.example.todaysbook.domain.entity.Cart;
import com.example.todaysbook.domain.entity.CartBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartBookRepository extends JpaRepository<CartBook, Long> {

    CartBook findByCartIdAndBookId(long cartId, long bookId);


    @Query("SELECT cb FROM CartBook cb WHERE cb.cart.userId = :userId")
    List<CartBook> findCartBooksByUserId(@Param("userId") Long userId);
}

