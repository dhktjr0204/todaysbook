package com.example.todaysbook.repository;

import com.example.todaysbook.domain.dto.BookDto;
import com.example.todaysbook.domain.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findByAuthorContainingOrTitleContaining(String author,String title, Pageable pageable);
    Page<Book> findAll(Pageable pageable);
    @Query("SELECT b " +
            "FROM Book b " +
            "WHERE b.categoryId = :categoryId " +
            "ORDER BY REGEXP_REPLACE(b.title, '[0-9]+.*', ''), " +
            "CAST(REGEXP_REPLACE(REGEXP_SUBSTR(b.title, '[^0-9]+[0-9]+[^+\\-():]*'),'[^0-9]','') AS int)")
    Page<Book> findAllByCategoryId(String categoryId, Pageable pageable);
    Optional<Book> findByTitle(String bookTitle);
    Optional<Book> findFirstByIsbn(String isbn);

    @Query("select new com.example.todaysbook.domain.dto.BookDto(" +
            "b.id, b.title, b.author, b.price, b.imagePath, b.publisher, b.publishDate, b.stock, b.isbn, b.description, b.categoryId) " +
            "from Book b " +
            "where b.categoryId=:categoryId " +
            "order by (" +
            "select sum(b.id * ob.bookCount) " +
            "from OrderBook ob " +
            "where ob.bookId=b.id) desc, " +
            "REGEXP_REPLACE(b.title, '[0-9]+.*', ''), " +
            "CAST(REGEXP_REPLACE(REGEXP_SUBSTR(b.title, '[^0-9]+[0-9]+[^+\\-():]*'),'[^0-9]','') AS int)" )
    Page<BookDto> findBookSortByBestSeller(String categoryId, Pageable pageable);

    @Query("select new com.example.todaysbook.domain.dto.BookDto(" +
            "b.id, b.title, b.author, b.price, b.imagePath, b.publisher, b.publishDate, b.stock, b.isbn, b.description, b.categoryId) " +
            "from Book b " +
            "where b.categoryId=:categoryId " +
            "order by (" +
            "select avg(r.score) " +
            "from Review r " +
            "WHERE r.bookId = b.id) DESC, " +
            "REGEXP_REPLACE(b.title, '[0-9]+.*', ''), " +
            "CAST(REGEXP_REPLACE(REGEXP_SUBSTR(b.title, '[^0-9]+[0-9]+[^+\\-():]*'),'[^0-9]','') AS int)" )
    Page<BookDto> findBookSortByReviewScore(String categoryId, Pageable pageable);
}
