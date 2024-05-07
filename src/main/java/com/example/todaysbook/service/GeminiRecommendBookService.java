package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.BookDto;
import com.example.todaysbook.domain.dto.GeminiRecommendApiRequest;
import com.example.todaysbook.domain.dto.GeminiRecommendApiResponse;
import com.example.todaysbook.domain.entity.Book;
import com.example.todaysbook.domain.entity.GeminiRecommendBook;
import com.example.todaysbook.repository.BookRepository;
import com.example.todaysbook.repository.GeminiRecommendBookRepository;
import lombok.RequiredArgsConstructor;
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
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public String getContents(String prompt) throws UnsupportedEncodingException {
        String requestUrl = apiUrl + "?key=" + geminiApiKey;

        GeminiRecommendApiRequest request = new GeminiRecommendApiRequest(prompt, candidateCount, maxOutputTokens, temperature);
        GeminiRecommendApiResponse response = restTemplate.postForObject(requestUrl, request, GeminiRecommendApiResponse.class);

        String message = response.getCandidates().get(0).getContent().getParts().get(0).getText().toString();

        System.out.println("-----------------응답 message-----------------\n" + message);

        saveGeminiRecommendBook(message);
        return message;
    }

    public void saveGeminiRecommendBook(String message) throws UnsupportedEncodingException {
        List<String> bookTitles = extractBookTitles(message);

        System.out.println("-----------------책 제목 DB 저장 시작-----------------");
        bookTitles.forEach(this::saveBookToDatabase);
    }

    private void saveBookToDatabase(String bookTitle) {
        try {
            Optional<Book> bookOptional = bookRepository.findByTitle(bookTitle);
            if (bookOptional.isPresent()) {
                saveGeminiRecommendBookWithBookId(bookOptional.get().getId(), bookTitle);
            } else {
                System.out.println(bookTitle + ": title로 검색해본 결과 DB에 없습니다. 외부 API에서 검색후 isbn을 가져와 다시 DB에 검색합니다.");

                String encodedTitle = URLEncoder.encode(bookTitle, "UTF-8");
                String apiUrl = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx?Query=" + encodedTitle + "&ttbkey=ttbcorsair171312001&MaxResults=1&start=1&SearchTarget=Book&Version=20131101&output=js&QueryType=Title&sort=Accuracy";

                BookDto bookDto = parseBookData(apiUrl);

                if (bookDto != null) {
                    Optional<Book> existingBook = bookRepository.findByIsbn(bookDto.getIsbn());
                    if (existingBook.isPresent()) {
                        saveGeminiRecommendBookWithBookId(existingBook.get().getId(), bookTitle);
                    } else {
                        System.out.println("    " + bookTitle + ": isbn으로 검색해본 결과 DB에 없는 책입니다. DB에 저장합니다.");
                        Book savedBook = saveBookToDatabase(bookDto);
                        saveGeminiRecommendBookWithBookId(savedBook.getId(), bookTitle);
                    }
                } else {
                    System.out.println("API 호출 결과가 없습니다. 다음 책으로 넘어갑니다.");
                }
            }
            System.out.println(" ");
        } catch (UnsupportedEncodingException e) {
            System.out.println("URL 인코딩 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    private void saveGeminiRecommendBookWithBookId(Long bookId, String bookTitle) {
        GeminiRecommendBook geminiRecommendBook = GeminiRecommendBook.builder()
                .bookId(bookId)
                .date(LocalDateTime.now())
                .build();
        geminiRecommendBookRepository.save(geminiRecommendBook);
        System.out.println(bookTitle + ": bookId(" + bookId + ") GeminiRecommendBook에 저장 완료");
    }

    private Book saveBookToDatabase(BookDto bookDto) {
        Book book = Book.builder()
                .title(bookDto.getTitle())
                .price(bookDto.getPrice())
                .author(bookDto.getAuthor())
                .publisher(bookDto.getPublisher())
                .publishDate(bookDto.getPublishDate())
                .stock(bookDto.getStock())
                .isbn(bookDto.getIsbn())
                .description(bookDto.getDescription())
                .imagePath(bookDto.getImagePath())
                .build();
        Book savedBook = bookRepository.save(book);
        System.out.println("    " + bookDto.getTitle() + ": 책 정보를 DB에 저장했습니다. bookId(" + savedBook.getId() + ")");
        return savedBook;
    }

    private List<String> extractBookTitles(String message) {
        System.out.println("---------------책 제목 추출 시작---------------");
        return Stream.of(message.split("\n"))
                .map(line -> line.replaceAll("^\\d+\\.\\s*", "").trim())
                .peek(System.out::println)
                .collect(Collectors.toList());
    }

    public List<BookDto> getTodayRecommendBooks() {
        LocalDate today = LocalDate.now();

        List<GeminiRecommendBook> todayRecommendBooks = geminiRecommendBookRepository.findByDateBetween(
                        today.atStartOfDay(), today.atTime(23, 59, 59))
                .stream()
                .distinct()
                .limit(10)
                .toList();

        List<Book> books = todayRecommendBooks.stream()
                .map(GeminiRecommendBook::getBookId)
                .distinct()
                .map(bookRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        return books.stream()
                .map(this::convertToBookDto)
                .collect(Collectors.toList());
    }

    private BookDto convertToBookDto(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .price(book.getPrice())
                .imagePath(book.getImagePath())
                .publisher(book.getPublisher())
                .publishDate(book.getPublishDate())
                .stock(book.getStock())
                .isbn(book.getIsbn())
                .description(book.getDescription())
                .category(book.getCategoryId())
                .build();
    }

    private BookDto parseBookData(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            br.close();

            JSONObject json = new JSONObject(result.toString());
            JSONArray items = json.getJSONArray("item");
            if (!items.isEmpty()) {
                JSONObject item = items.getJSONObject(0);

                return BookDto.builder()
                        .title(item.getString("title"))
                        .price(item.getLong("priceSales"))
                        .author(item.getString("author"))
                        .publisher(item.getString("publisher"))
                        .publishDate(LocalDate.parse(item.getString("pubDate")))
                        .isbn(item.getString("isbn13"))
                        .description(item.getString("description"))
                        .imagePath(item.getString("cover"))
                        .category(item.getString("categoryName"))
                        .build();
            }
        } catch (IOException e) {
            System.out.println("API 호출 중 오류가 발생했습니다: " + e.getMessage());
        } catch (JSONException e) {
            System.out.println("JSON 파싱 중 오류가 발생했습니다: " + e.getMessage());
        }

        return null;
    }
}