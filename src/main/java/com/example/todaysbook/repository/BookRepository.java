package com.example.todaysbook.repository;

import com.example.todaysbook.domain.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findByAuthorContainingOrTitleContaining(String author,String title, Pageable pageable);
    Page<Book> findAll(Pageable pageable);
    Optional<Book> findByTitle(String bookTitle);
}
