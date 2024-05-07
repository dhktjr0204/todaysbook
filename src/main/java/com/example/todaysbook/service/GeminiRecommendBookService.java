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
import java.time.format.DateTimeFormatter;
import java.util.*;
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

    @Value("${aladin.ttbkey}")
    private String ttbkey;

    private final BookRepository bookRepository;
    private final GeminiRecommendBookRepository geminiRecommendBookRepository;
    private AdminServiceImpl adminService;

    @Autowired
    public GeminiRecommendBookService(BookRepository bookRepository, GeminiRecommendBookRepository geminiRecommendBookRepository, AdminServiceImpl adminService) {
        this.bookRepository = bookRepository;
        this.geminiRecommendBookRepository = geminiRecommendBookRepository;
        this.adminService = adminService;
    }

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

                // 가져온 bookId를 GeminiRecommendBook 엔티티에 저장
                saveGeminiRecommendBookEntity(bookId);
                System.out.println(bookTitle + "  : bookId(" + bookId + ") GeminiRecommendBook에 저장 완료");

            } else {
                // DB에 책이 없으면 외부 API에서 데이터 가져와서 저장
                System.out.println(bookTitle + ":  title로 검색해본 결과 DB에 없습니다. 외부 API에서 검색후 isbn을 가져와 다시 DB에 검색합니다.");

                HashMap<String, ?> response = adminService.getNewBook(bookTitle, 1);
                List<BookDto> bookList = (List<BookDto>) response.get("books");

                if (!bookList.isEmpty()) {
                    BookDto bookDto = bookList.get(0);
                    Optional<Book> existingBook = bookRepository.findByIsbn(bookDto.getIsbn());
                    if (existingBook.isPresent()) {
                        bookId = existingBook.get().getId();
                        saveGeminiRecommendBookEntity(bookId);
                    } else {
                        adminService.addNewBook(List.of(bookDto));
                        Optional<Book> savedBook = bookRepository.findByIsbn(bookDto.getIsbn());
                        if (savedBook.isPresent()) {
                            bookId = savedBook.get().getId();
                            saveGeminiRecommendBookEntity(bookId);
                        }
                    }
                } else {
                    System.out.println("    API 호출 결과가 없습니다.");
                }
            }
            System.out.println(" ");
        }
    }

    private void saveGeminiRecommendBookEntity(long bookId) {
        GeminiRecommendBook geminiRecommendBook = GeminiRecommendBook.builder()
                .bookId(bookId)
                .date(LocalDateTime.now())
                .build();
        geminiRecommendBookRepository.save(geminiRecommendBook);
        System.out.println("bookId(" + bookId + ") GeminiRecommendBook에 저장 완료");
    }


    private List<String> extractBookTitles(String message) {
        System.out.println("---------------책 제목 추출 시작---------------");
        return Arrays.stream(message.split("\n"))
                .map(line -> line.replaceAll("^\\d+\\.\\s*", "").trim())
                .peek(System.out::println)
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
                //.limit(10)
                .toList();


        // 오늘 날짜에 해당하는 bookId 목록 가져오기
        List<Book> books = todayRecommendBooks.stream()
                .map(GeminiRecommendBook::getBookId)
                .distinct()
                .map(bookRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        // book 목록을 BookDto 목록으로 변환
        return books.stream()
                .map(book -> new BookDto(book.getId(), book.getTitle(), book.getAuthor(), book.getPrice(), book.getImagePath(), book.getPublisher(), book.getPublishDate(), book.getStock(), book.getIsbn(), book.getDescription(), book.getImagePath(), book.getCategoryId()))
                .collect(Collectors.toList());
    }


    // 외부 API 호출 결과를 파싱하여 BookDto 객체로 변환하는 메소드
    private BookDto parseBookData(String apiUrl) {
        try {
            // API 호출
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");

            // 응답 읽기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            br.close();

            // JSON 파싱
            JSONObject json = new JSONObject(result.toString());
            JSONArray items = json.getJSONArray("item");
            if (!items.isEmpty()) {
                JSONObject item = items.getJSONObject(0);

                // BookDto 객체에 데이터 저장
                return BookDto.builder()
                        .title(item.getString("title"))
                        .price(item.getLong("priceSales"))
                        .author(item.getString("author"))
                        .publisher(item.getString("publisher"))
                        .publishDate(LocalDate.parse(item.getString("pubDate"))) // LocalDate로 변경
                        .isbn(item.getString("isbn13"))
                        .description(item.getString("description"))
                        .imagePath(item.getString("cover"))
                        .category(adminService.convertCategoryToCategoryId(item.getString("categoryName")))
                        .build();
            }
        } catch (IOException e) {
            System.out.println("API 호출 중 오류가 발생했습니다: " + e.getMessage());
            return null;
        } catch (JSONException e) {
            System.out.println("JSON 파싱 중 오류가 발생했습니다: " + e.getMessage());
            return null;
        }

        return null;
    }
}