package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.AlanRecommendBookDto;
import com.example.todaysbook.domain.entity.AlanRecommendBook;
import com.example.todaysbook.domain.entity.AlanRecommendData;
import com.example.todaysbook.domain.entity.Book;
import com.example.todaysbook.repository.AlanRecommendBookRepository;
import com.example.todaysbook.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlanRecommendBookService {

    private final AlanRecommendBookRepository alanRecommendBookRepository;
    private final BookRepository bookRepository;

    public void saveAlanRecommendBooks(List<AlanRecommendData> data) { 
        // List<AlanRecommendData>로 받아오면 나중에 데이터가 많아지면 시간이 많이 걸릴것 같음
        for (AlanRecommendData element : data) {
            Optional<Book> existingBook = bookRepository.findByTitle(element.getTitle()); // findby 바꾸기
            if (existingBook.isPresent()) {
                // BookRepository에 이미 책이 있는 경우, 그대로 bookRepository의 book을 AlanRecommendBookRepository에 저장
                AlanRecommendBook alanRecommendBook = AlanRecommendBookDto.createFromBook(existingBook.get());
                alanRecommendBookRepository.save(alanRecommendBook);
                // TODO: Book 데이터 넣어서 동일한 젝목이 book에 있을때 AlanRecommendBook이 잘 저장되는지 테스트 해보기
            } else {
                // TODO: BookRepository에 책이 없는 경우, 외부 API(중앙도서관)를 사용하여 책 정보 가져오기
                System.out.println("BookRepository에 책이 없는 경우, 외부 API(중앙도서관)를 사용하여 책 정보 가져오기");
            }
        }
    }

    /*
    * TODO: 외부 API(중앙도서관)를 사용하여 책 정보 가져오는 메서드 구현 (따로 클래스로 빼서 구현해야 할듯)
    * */

}