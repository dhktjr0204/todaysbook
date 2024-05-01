package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.AlanRecommendBookDto;
import com.example.todaysbook.domain.entity.AlanRecommendBook;
import com.example.todaysbook.domain.entity.AlanRecommendData;
import com.example.todaysbook.domain.entity.Book;
import com.example.todaysbook.repository.AlanRecommendBookRepository;
import com.example.todaysbook.repository.AlanRecommendDataRepository;
import com.example.todaysbook.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlanRecommendBookService {

    private final AlanRecommendDataRepository alanRecommendDataRepository;
    private final AlanRecommendBookRepository alanRecommendBookRepository;
    private final BookRepository bookRepository;

    public void saveAlanRecommendBooks(List<AlanRecommendData> data) {

        /*
         * TODO: date가 오늘 날짜인 것만 가져오기
         *  List<AlanRecommendData>로 받아오면 나중에 데이터가 많아지면 성능이슈가 있을 수 있음.
         *  -> 해당 날짜 데이터만 가져오기
         * */
        LocalDate today = LocalDate.now();
        List<AlanRecommendData> alanRecommendDataList = alanRecommendDataRepository.findByCreatedAtBetween(
                today.atStartOfDay(), today.plusDays(1).atStartOfDay());


        /*
         * TODO: BOOK테이블에 책 제목으로 검색하여 책이 이미 존재하는지 확인하는 방법 계속 고민하기
         *  ISBN이나 다른 고유한 값이 없기 때문에 책 제목으로만 검색하는 방법을 사용해야 한다.
         *  하지만 책 제목은 고유하지 않을 수 있으므로, 검색 결과가 여러 개일 수 있다.
         *  검색 결과 중에서 가장 같은것을 찾아서 사용해야 한다.
         * */
        for (AlanRecommendData element : data) {
            Optional<Book> existingBook = bookRepository.findByTitle(element.getTitle()); // findby 바꾸기
            if (existingBook.isPresent()) {
                // BookRepository에 이미 책이 있는 경우, 그대로 bookRepository의 book을 AlanRecommendBookRepository에 저장
                AlanRecommendBook alanRecommendBook = AlanRecommendBookDto.createFromBook(existingBook.get());
                alanRecommendBookRepository.save(alanRecommendBook);
            } else {
                // TODO: BookRepository에 책이 없는 경우, 외부 API(중앙도서관)를 사용하여 책 정보 가져오기

            }
        }
        for (AlanRecommendData alanRecommendData : alanRecommendDataList) {
            String title = alanRecommendData.getTitle();

            Optional<Book> bookOptional = bookRepository.findByTitle(title);

            if (bookOptional.isPresent()) {
                Book book = bookOptional.get();
                AlanRecommendBook alanRecommendBook = AlanRecommendBookDto.createFromBook(book);
                alanRecommendBookRepository.save(alanRecommendBook);
                // TODO: Book에 찾는 책 제목이 있을때 AlanRecommendBook이 잘 저장되는지 테스트 해보기
            } else {
                // TODO: 외부 API(알라딘 등)를 사용하여 책 정보 가져와서 AlanRecommendBook에 저장
                // 외부 API를 사용하는 로직을 구현해야 합니다.
                // 가져온 책 정보를 AlanRecommendBook 엔티티에 저장합니다.
            }
        }
    }

        public List<AlanRecommendBookDto> getAlanRecommendBooks() {
            List<AlanRecommendBook> alanRecommendBooks = alanRecommendBookRepository.findAll();
            List<AlanRecommendBookDto> alanRecommendBookDtos = new ArrayList<>();

            for (AlanRecommendBook alanRecommendBook : alanRecommendBooks) {
                AlanRecommendBookDto alanRecommendBookDto = alanRecommendBook.toDto();
                alanRecommendBookDtos.add(alanRecommendBookDto);
            }

            return alanRecommendBookDtos;
        }


    }

    /*
    * TODO: 외부 API(중앙도서관)를 사용하여 책 정보 가져오는 메서드 구현 (따로 클래스로 빼서 구현해야 할듯)
    * */

