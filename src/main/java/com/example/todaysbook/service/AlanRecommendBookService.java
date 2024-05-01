package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.AlanRecommendBookDto;
import com.example.todaysbook.domain.dto.AlanRecommendDataDto;
import com.example.todaysbook.domain.entity.AlanRecommendBook;
import com.example.todaysbook.domain.entity.Book;
import com.example.todaysbook.repository.AlanRecommendBookRepository;
import com.example.todaysbook.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlanRecommendBookService {

    private final AlanRecommendDataService alanRecommendDataService;
    private final AlanRecommendBookRepository alanRecommendBookRepository;
    private final BookRepository bookRepository;
    private static final Logger logger = LoggerFactory.getLogger(AlanRecommendBookService.class);
    public void saveAlanRecommendBooks() {
        List<AlanRecommendDataDto> alanRecommendDataList = alanRecommendDataService.getTodaysBooks();

        //오늘의 책 추천 데이터 모두 출력
        logger.info("\n오늘의 책 추천 데이터: {}", alanRecommendDataList.stream().map(AlanRecommendDataDto::getTitle).collect(Collectors.toList()));

        for (AlanRecommendDataDto element : alanRecommendDataList) {
            Optional<Book> existingBook = bookRepository.findByTitle(element.getTitle());

            if (existingBook.isPresent()) {
                AlanRecommendBook alanRecommendBook = AlanRecommendBookDto.createFromBook(existingBook.get());
                alanRecommendBookRepository.save(alanRecommendBook);
            } else {
                // TODO: BookRepository에 책이 없는 경우, 외부 API(중앙도서관)를 사용하여 책 정보 가져오기
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

/*
 * TODO: BOOK테이블에 책 제목으로 검색하여 책이 이미 존재하는지 확인하는 방법 계속 고민하기
 *  ISBN이나 다른 고유한 값이 없기 때문에 책 제목으로만 검색하는 방법을 사용해야 한다.
 *  하지만 책 제목은 고유하지 않을 수 있으므로, 검색 결과가 여러 개일 수 있다.
 *  검색 결과 중에서 가장 같은것을 찾아서 사용해야 한다.
 * */