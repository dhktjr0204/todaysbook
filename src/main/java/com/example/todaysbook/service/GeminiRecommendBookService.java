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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
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

    private final BookRepository bookRepository;
    private final GeminiRecommendBookRepository geminiRecommendBookRepository;

    private final AladinApi aladinApi;
    private final AdminServiceImpl adminService;

    private String defaultPrompt = "오늘 한국 기준으로 최근에 많이 팔린 책의 제목 15개를 추천해 주세요. 신뢰할 수 있는 최신 정보를 바탕으로 정확한 책 제목만 나열하여 주세요. 존재하지 않는 책 제목은 추천하지 마세요. 답변에는 책의 저자, 출처, 참고, 이미지 등 다른 내용은 포함하지 말아주세요.";
    private int defaultQuantity = 15;
    private double defaultTemperature = 0.5;

    public ResponseEntity<String> callScheduledGeminiApi() {
        return callGeminiApi(defaultPrompt, defaultQuantity, defaultTemperature);
    }

    public void recommendAndSaveBooks(Integer quantity, Double temperature) throws UnsupportedEncodingException {
        quantity = quantity != null ? quantity : defaultQuantity;
        temperature = temperature != null ? temperature : defaultTemperature;

        String prompt = "한국 기준으로 최근에 많이 팔린 책의 제목 " + quantity + "개를 추천해 주세요. 신뢰할 수 있는 최신 정보를 바탕으로 정확한 책 제목만 나열하여 주세요. 존재하지 않는 책 제목은 추천하지 마세요. 답변에는 책의 저자, 출처, 참고, 이미지 등 다른 내용은 포함하지 말아주세요.";
        callGeminiApi(prompt, quantity, temperature);
    }

    private ResponseEntity<String> callGeminiApi(String prompt, int quantity, double temperature) {
        try {
            String message = getContents(prompt, temperature);
            saveGeminiRecommendBook(message);
            return ResponseEntity.ok(message);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    private String getContents(String prompt, double temperature) throws UnsupportedEncodingException {
        String requestUrl = apiUrl + "?key=" + geminiApiKey;
        GeminiRecommendApiRequest request = new GeminiRecommendApiRequest(prompt, candidateCount, maxOutputTokens, temperature);
        GeminiRecommendApiResponse response = restTemplate.postForObject(requestUrl, request, GeminiRecommendApiResponse.class);
        return response.getCandidates().get(0).getContent().getParts().get(0).getText();
    }

    public void saveGeminiRecommendBook(String message) throws UnsupportedEncodingException {
        // 책 제목 추출
        List<String> bookTitles = extractBookTitles(message);


        log.info("-----------------책 제목 DB 저장 시작-----------------");
        for (String bookTitle : bookTitles) {
            // 책 제목으로 DB 검색
            Optional<Book> bookOptional = bookRepository.findFirstByTitle(bookTitle);
            long bookId;
            if (bookOptional.isPresent()) {
                // DB에 책이 있으면 해당 bookId 가져오기
                bookId = bookOptional.get().getId();
                log.info(bookTitle + ": bookId(" + bookId + ") DB에 있습니다.");
                // 가져온 bookId를 GeminiRecommendBook 엔티티에 저장
                saveGeminiRecommendBookEntity(bookId);

            } else {
                // DB에 책이 없으면 외부 API에서 데이터 가져와서 저장
                log.info(bookTitle + ":  title로 검색해본 결과 Book에 없습니다. 외부 API에서 검색후 isbn을 가져와 다시 검색합니다.");

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
                    log.info(bookTitle + ":  isbn으로 검색해본 결과 외부 API에서도 없습니다. 다음 책으로 넘어갑니다.\n");
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
                .map(book -> new BookDto(book.getId(), book.getTitle(), book.getAuthor(), book.getPrice(), book.getImagePath(), book.getPublisher(), book.getPublishDate(), book.getStock(), book.getIsbn(), book.getDescription(), book.getCategoryId()))
                .collect(Collectors.toList());
    }
}