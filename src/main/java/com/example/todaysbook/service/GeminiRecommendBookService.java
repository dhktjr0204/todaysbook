package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.BookDto;
import com.example.todaysbook.domain.dto.GeminiRecommendApiRequest;
import com.example.todaysbook.domain.dto.GeminiRecommendApiResponse;
import com.example.todaysbook.domain.entity.Book;
import com.example.todaysbook.domain.entity.GeminiRecommendBook;
import com.example.todaysbook.repository.BookRepository;
import com.example.todaysbook.repository.GeminiRecommendBookRepository;
import com.example.todaysbook.util.AladinApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiRecommendBookService {

    @Qualifier("geminiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.generationConfig.candidate_count}")
    private int candidateCount;

    @Value("${gemini.generationConfig.max_output_tokens}")
    private int maxOutputTokens;

    @Value("${gemini.generationConfig.temperature}")
    private double temperature;

    private final BookRepository bookRepository;
    private final GeminiRecommendBookRepository geminiRecommendBookRepository;

    private final AladinApi aladinApi;
    private final AdminServiceImpl adminService;

    public String getContents(String prompt) throws UnsupportedEncodingException {
        String requestUrl = apiUrl + "?key=" + geminiApiKey;

        GeminiRecommendApiRequest request = new GeminiRecommendApiRequest(prompt, candidateCount, maxOutputTokens, temperature);
        GeminiRecommendApiResponse response = restTemplate.postForObject(requestUrl, request, GeminiRecommendApiResponse.class);

        String message = response.getCandidates().get(0).getContent().getParts().get(0).getText().toString();

//        log.info("-----------------응답 message-----------------\n" + message);

        saveGeminiRecommendBook(message);
        return message;
    }


    // 나중에 adminService가 아닌 유틸 클래스 만들어 지면 그것을 사용하도록 변경
    // max 매개변수 설정하는거 기억 (프롬프트 책 15개 이상으로 변경 하려고 하기 때문에)
    public void saveGeminiRecommendBook(String message) throws UnsupportedEncodingException {
        // 책 제목 추출
        List<String> bookTitles = extractBookTitles(message);


        log.info("-----------------책 제목 DB 저장 시작-----------------");
        for (String bookTitle : bookTitles) {
            // 책 제목으로 DB 검색
            Optional<Book> bookOptional = bookRepository.findByTitle(bookTitle);
            long bookId;
            if (bookOptional.isPresent()) {
                // DB에 책이 있으면 해당 bookId 가져오기
                bookId = bookOptional.get().getId();
                log.info(bookTitle + ": bookId(" + bookId + ") DB에 있습니다.");
                // 가져온 bookId를 GeminiRecommendBook 엔티티에 저장
                saveGeminiRecommendBookEntity(bookId);

            } else {
                // DB에 책이 없으면 외부 API에서 데이터 가져와서 저장
                log.info(bookTitle + ":  title로 검색해본 결과 Book테이블에 없습니다. 외부 API에서 검색후 isbn을 가져와 다시 검색합니다.");

                HashMap<String, ?> response = aladinApi.getNewBook(bookTitle, 1,1);
                List<BookDto> bookList = (List<BookDto>) response.get("books");

                if (!bookList.isEmpty()) { // API 호출 결과가 있으면
                    BookDto bookDto = bookList.get(0); // 첫 번째 결과 사용
                    Optional<Book> existingBook = bookRepository.findFirstByIsbn(bookDto.getIsbn()); // isbn으로 DB 검색
                    if (existingBook.isPresent()) { // isbn으로 DB 검색 결과 해당 있으면 해당 bookId 가져오기 (즉, 두번 검색)(첫번째는 제목으로, 두번째는 isbn으로)
                        log.info(bookTitle + ":  isbn으로 검색해본 결과 Book에 있습니다.");
                        bookId = existingBook.get().getId();
                        saveGeminiRecommendBookEntity(bookId);
                    } else {
                        log.info(bookTitle + ":  isbn으로 검색해본 결과 Book에 없습니다. 외부 API에서 가져온 데이터를 Book에 저장합니다.");
                        aladinApi.addNewBook(bookDto);
                        Optional<Book> savedBook = bookRepository.findFirstByIsbn(bookDto.getIsbn());
                        if (savedBook.isPresent()) {
                            bookId = savedBook.get().getId();
                            log.info("bookId(" + bookId + ") Book에 저장 완료");
                            saveGeminiRecommendBookEntity(bookId);
                        }
                    }
                } else {
                    log.info("API 호출 결과가 없습니다. 다음 책으로 넘어갑니다.\n");
                }
            }
        }
        log.info("-----------------책 제목 DB 저장 완료-----------------\n");
    }

    private void saveGeminiRecommendBookEntity(long bookId) {
        GeminiRecommendBook geminiRecommendBook = GeminiRecommendBook.builder()
                .bookId(bookId)
                .date(LocalDateTime.now())
                .build();
        geminiRecommendBookRepository.save(geminiRecommendBook);
        log.info("bookId(" + bookId + ") GeminiRecommendBook에 저장 완료\n");
    }


    private List<String> extractBookTitles(String message) {
        log.info("---------------책 제목 추출 시작---------------");
        return Arrays.stream(message.split("\n"))
                .map(line -> line.replaceAll("^\\d+\\.\\s*", "").trim())
                .peek(log::info)
                .collect(Collectors.toList());
    }


    // GeminiRecommendBook 목록에서 date가 오늘에 해당하는 booid를 이용해 해당 book을 반환하는 메소드
    public List<BookDto> getTodayRecommendBooks() {
        LocalDate today = LocalDate.now();

        // 오늘 날짜에 해당하는 GeminiRecommendBook 목록 가져오고 bookId 중복 제거 후 10개만 가져오기
        List<GeminiRecommendBook> todayRecommendBooks = geminiRecommendBookRepository.findByDateBetween(
                        today.atStartOfDay(), today.atTime(23, 59, 59))
                .stream()
                .distinct()
                .limit(10)
                .toList();


        // 오늘 날짜에 해당하는 bookId 목록 가져오기
        List<Book> books = todayRecommendBooks.stream()
                .map(GeminiRecommendBook::getBookId)
                .distinct()
                .map(bookRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .limit(10)
                .toList();

        // book 목록을 BookDto 목록으로 변환
        return books.stream()
                .map(book -> new BookDto(book.getId(), book.getTitle(), book.getAuthor(), book.getPrice(), book.getImagePath(), book.getPublisher(), book.getPublishDate(), book.getStock(), book.getIsbn(), book.getDescription(), book.getImagePath(), book.getCategoryId()))
                .collect(Collectors.toList());
    }

}