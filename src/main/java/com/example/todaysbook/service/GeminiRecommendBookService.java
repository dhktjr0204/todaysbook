package com.example.todaysbook.service;
import com.example.todaysbook.domain.dto.BookDto;
import com.example.todaysbook.domain.dto.GeminiRecommendApiRequest;
import com.example.todaysbook.domain.dto.GeminiRecommendApiResponse;
import com.example.todaysbook.domain.entity.Book;
import com.example.todaysbook.domain.entity.GeminiRecommendBook;
import com.example.todaysbook.repository.BookRepository;
import com.example.todaysbook.repository.GeminiRecommendBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeminiRecommendBookService { // 설명: GeminiService 클래스는 Gemini API를 호출하는 서비스 클래스입니다.

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

    public String getContents(String prompt) {
        String requestUrl = apiUrl + "?key=" + geminiApiKey;

        GeminiRecommendApiRequest request = new GeminiRecommendApiRequest(prompt, candidateCount, maxOutputTokens, temperature);
        GeminiRecommendApiResponse response = restTemplate.postForObject(requestUrl, request, GeminiRecommendApiResponse.class);

        String message = response.getCandidates().get(0).getContent().getParts().get(0).getText().toString();

        System.out.println("-----------------응답 message-----------------\n" + message);

        saveGeminiRecommendBook(message);
        return message;
    }

    public void saveGeminiRecommendBook(String message) {
        // 책 제목 추출
        List<String> bookTitles = extractBookTitles(message);


        System.out.println("-----------------책 제목 DB 저장 시작-----------------");
        for (String bookTitle : bookTitles) {
            // 책 제목으로 DB 검색
            Optional<Book> bookOptional = bookRepository.findByTitle(bookTitle);
            long bookId;
            if (bookOptional.isPresent()) {
                // DB에 책이 있으면 해당 bookId 가져오기
                bookId = bookOptional.get().getId();

                System.out.println(bookTitle + "  : bookId(" + bookId + ") GeminiRecommendBook에 저장 완료");

                // 가져온 bookId를 GeminiRecommendBook 엔티티에 저장
                GeminiRecommendBook geminiRecommendBook = GeminiRecommendBook.builder()
                        .bookId(bookId) // variable 'bookId' is not initialized
                        .date(LocalDateTime.now())
                        .build();
                geminiRecommendBookRepository.save(geminiRecommendBook);
            } else {
                // DB에 책이 없으면 외부 API에서 데이터 가져와서 저장
                System.out.println(bookTitle + ":  해당 책이 DB에 없습니다. 외부 API에서 데이터를 book에 저장후 해당 Bookid를 가져옵니다.***");
                // 가져온 bookId를 GeminiRecommendBook 엔티티에 저장
            }

        }

        System.out.println("가져온 Bookid를 GeminiRecommendBook에 모두 저장 완료.");
    }

    private List<String> extractBookTitles(String message) {
        System.out.println("---------------책 제목 추출 시작---------------");
        List<String> bookTitles = Arrays.stream(message.split("\n"))
                .map(line -> line.replaceAll("^\\d+\\.\\s*", "").trim())
                .peek(System.out::println)
                .collect(Collectors.toList());
        return bookTitles;
    }


    // GeminiRecommendBook 목록에서 date가 오늘에 해당하는 booid를 이용해 해당 book을 반환하는 메소드
    public List<BookDto> getTodayRecommendBooks() {
        LocalDate today = LocalDate.now();

        List<GeminiRecommendBook> todayRecommendBooks = geminiRecommendBookRepository.findByDateBetween(
                today.atStartOfDay(), today.plusDays(1).atStartOfDay());

        List<Book> books = todayRecommendBooks.stream()
                .map(recommendBook -> bookRepository.findById(recommendBook.getBookId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return books.stream()
                .map(book -> new BookDto(book.getId(), book.getTitle(), book.getAuthor(), book.getPrice(), book.getImagePath(), book.getPublisher(), book.getPublishDate(), book.getStock(), book.getIsbn(), book.getDescription(), book.getImagePath()))
                .collect(Collectors.toList());
    }

}