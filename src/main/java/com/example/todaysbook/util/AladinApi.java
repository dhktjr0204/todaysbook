package com.example.todaysbook.util;

import com.example.todaysbook.domain.CategoryEnum;
import com.example.todaysbook.domain.dto.BookDto;
import com.example.todaysbook.domain.entity.Book;
import com.example.todaysbook.exception.aladinApi.NotValidBook;
import com.example.todaysbook.exception.book.DuplicateBookException;
import com.example.todaysbook.repository.BookRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AladinApi {

    @Value("${aladin.ttbkey}")
    private String aladinKey;
    private final ObjectMapper objectMapper;
    private final BookRepository bookRepository;

    public HashMap<String, ?> getNewBook(String keyword, int page, int size) {
        String url = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx?Query=" + keyword +
                "&ttbkey=" + aladinKey +
                "&MaxResults=" + size +
                "&start=" + page +
                "&SearchTarget=Book&Version=20131101&output=js" +
                "&QueryType=Title&sort=Accuracy&Cover=Big";


        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        String jsonResponse = responseEntity.getBody();

        List<BookDto> books = new ArrayList<>();
        int totalResults = 0;

        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode itemsNode = rootNode.get("item");

            if (itemsNode != null && itemsNode.isArray()) {
                for (JsonNode itemNode : itemsNode) {
                    BookDto bookDto = convertJsonToBookDto(itemNode);

                    books.add(bookDto);
                }
                totalResults = rootNode.get("totalResults").asInt();
            }
        } catch (Exception e) {
            throw new NotValidBook();
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("books", books);
        map.put("totalPage", (int) Math.ceil(totalResults / 10.0));

        return map;
    }

    public void addNewBook(BookDto bookDto) {
        Optional<Book> existingBook = bookRepository.findFirstByIsbn(bookDto.getIsbn());
        if (existingBook.isPresent()) {
            throw new DuplicateBookException();
        }

        Book newBook = Book.builder()
                .title(bookDto.getTitle())
                .price(bookDto.getPrice())
                .author(bookDto.getAuthor())
                .description(bookDto.getDescription())
                .publisher(bookDto.getPublisher())
                .publishDate(bookDto.getPublishDate())
                .stock(bookDto.getStock())
                .isbn(bookDto.getIsbn())
                .imagePath(bookDto.getImage())
                .categoryId(bookDto.getCategory())
                .build();

        bookRepository.save(newBook);
    }
    private BookDto convertJsonToBookDto(JsonNode itemNode) {
        BookDto book = BookDto.builder()
                .title(itemNode.get("title").asText())
                .author(itemNode.get("author").asText())
                .price(itemNode.get("priceStandard").asLong())
                .image(itemNode.get("cover").asText())
                .publisher(itemNode.get("publisher").asText())
                .publishDate(dateFomatter(itemNode.get("pubDate").asText()))
                .isbn(itemNode.get("isbn13").asText())
                .category(convertCategoryToCategoryId(itemNode.get("categoryName").asText()))
                .stock(-1L)
                .description(itemNode.get("description").asText())
                .build();


        return book;
    }

    private LocalDate dateFomatter(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate; //날짜 + 00:00:00.00000000을 의미
    }

    public String convertCategoryToCategoryId(String category) {
        //카테고리가 빈값일 때
        if (category.isEmpty()) {
            return CategoryEnum.ETC.getKey();
        }

        String[] splitCategory = category.split(">");

        String categoryName = splitCategory[1];

        // CategoryEnum에서 해당하는 카테고리를 찾아서 key를 반환
        for (CategoryEnum categoryEnum : CategoryEnum.values()) {
            if (categoryEnum.getTitle().equals(categoryName)) {
                return categoryEnum.getKey();
            }
        }

        // 일치하는 카테고리가 없을 경우 기타로 반환
        return CategoryEnum.ETC.getKey();
    }
}
