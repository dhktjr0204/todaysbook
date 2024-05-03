package com.example.todaysbook.repository;

import com.example.todaysbook.domain.dto.FavoriteBookDTO;
import com.example.todaysbook.domain.entity.FavoriteBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteBookRepository extends JpaRepository<FavoriteBook, Long> {

    @Query(value = "select new com.example.todaysbook.domain.dto.FavoriteBookDTO( " +
            "b.id, b.title, b.author, b.price, b.imagePath) " +
            "from FavoriteBook fb " +
            "left join Book b " +
            "on b.id = fb.bookId " +
            "where fb.userId = :userId")
    Page<FavoriteBookDTO> findFavoriteBooksByUserId(Long userId, Pageable pageable);
}
