package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.AdminUserDto;
import com.example.todaysbook.domain.dto.BookDto;
import com.example.todaysbook.domain.entity.Book;
import com.example.todaysbook.domain.entity.User;
import com.example.todaysbook.exception.aladinApi.NotValidBook;
import com.example.todaysbook.repository.BookRepository;
import com.example.todaysbook.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Value("${aladin.ttbkey}")
    private String aladinKey;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Page<AdminUserDto> findAllUser(Pageable pageable) {
        Page<User> userList = userRepository.findAll(pageable);
        return userList.map(this::convertUserToDto);
    }

    @Override
    public Page<AdminUserDto> findUsersByKeyword(String keyword, Pageable pageable) {
        Page<User> userList = userRepository
                .findByEmailContainingOrNickNameContaining(keyword, keyword, pageable);

        if (userList.isEmpty()) {
            return Page.empty();
        }

        return userList.map(this::convertUserToDto);
    }

    @Override
    public Page<BookDto> findAllBook(Pageable pageable) {
        Page<Book> bookList = bookRepository.findAll(pageable);

        return bookList.map(this::convertBookToDto);
    }

    @Override
    public Page<BookDto> findBooksByKeyword(String keyword, Pageable pageable) {
        Page<Book> bookList = bookRepository.findByAuthorContainingOrTitleContaining(keyword, keyword, pageable);

        if (bookList.isEmpty()) {
            return Page.empty();
        }

        return bookList.map(this::convertBookToDto);
    }

    @Override
    public BookDto findBookById(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow();

        return BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .price(book.getPrice())
                .publisher(book.getPublisher())
                .publishDate(book.getPublishDate())
                .stock(book.getStock())
                .description(book.getDescription())
                .image(book.getImagePath())
                .build();
    }

    @Transactional
    @Override
    public void updateUserRole(Long userId, String role) {
        User user = userRepository.findById(userId)
                .orElseThrow();

        user.updateRole(role);
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {

        userRepository.deleteById(userId);

    }

    @Transactional
    @Override
    public void updateBookStock(Long bookId, Long stock) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow();

        book.updateStock(stock);
    }

    @Transactional
    @Override
    public void deleteBook(Long bookId) {

        bookRepository.deleteById(bookId);

    }

    @Transactional
    @Override
    public void updateBook(BookDto bookDto) {
        Book book = bookRepository.findById(bookDto.getId())
                .orElseThrow();

        book.updateBook(bookDto.getTitle(), bookDto.getPrice(), bookDto.getAuthor(),
                bookDto.getPublisher(), bookDto.getStock(), bookDto.getDescription());
    }

    @Override
    public HashMap<String, ?> getNewBook(String keyword, int page) {

        String url = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx?Query=" + keyword +
                "&ttbkey=" + aladinKey +
                "&MaxResults=10&start=" + page +
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

    @Override
    public void addNewBook(List<BookDto> bookList) {
        for (BookDto book : bookList) {
            Book newBook = Book.builder()
                    .title(book.getTitle())
                    .price(book.getPrice())
                    .author(book.getAuthor())
                    .description(book.getDescription())
                    .publisher(book.getPublisher())
                    .publishDate(book.getPublishDate())
                    .stock(book.getStock())
                    .isbn(book.getIsbn())
                    .imagePath(book.getImage())
                    .categoryId(book.getCategory())
                    .build();

            bookRepository.save(newBook);
        }
    }

    private AdminUserDto convertUserToDto(User user) {
        return AdminUserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickName(user.getNickName())
                .grade(user.getRole())
                .build();
    }

    private BookDto convertBookToDto(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .price(book.getPrice())
                .stock(book.getStock())
                .build();
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

    private LocalDateTime dateFomatter(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate.atStartOfDay(); //날짜 + 00:00:00.00000000을 의미
    }

    private String convertCategoryToCategoryId(String category) {
        //카테고리가 빈값일 때
        if (category.isEmpty()) {
            return "123";
        }

        String[] splitCategory = category.split(">");

        String categoryName = splitCategory[1];
        if (categoryName.equals("자기계발")) {
            return "101";
        } else if (categoryName.equals("어린이")) {
            return "102";
        } else if (categoryName.equals("외국어")) {
            return "103";
        } else if (categoryName.equals("인문학")) {
            return "104";
        } else if (categoryName.equals("유아")) {
            return "105";
        } else if (categoryName.equals("만화")) {
            return "106";
        } else if (categoryName.equals("경제경영")) {
            return "107";
        } else if (categoryName.equals("소설/시/희곡")) {
            return "108";
        } else if (categoryName.equals("에세이")) {
            return "109";
        } else if (categoryName.equals("수험서/자격증")) {
            return "110";
        } else if (categoryName.equals("예술/대중문화")) {
            return "111";
        } else if (categoryName.equals("컴퓨터/모바일")) {
            return "112";
        } else if (categoryName.equals("청소년")) {
            return "113";
        } else if (categoryName.equals("과학")) {
            return "114";
        } else if (categoryName.equals("사회과학")) {
            return "115";
        } else if (categoryName.equals("좋은부모")) {
            return "116";
        } else if (categoryName.equals("건강/취미")) {
            return "117";
        } else if (categoryName.equals("역사")) {
            return "118";
        } else if (categoryName.equals("종교/역학")) {
            return "119";
        } else if (categoryName.equals("요리/살림")) {
            return "120";
        } else if (categoryName.equals("여행")) {
            return "121";
        } else if (categoryName.equals("대학교재/전문서적")) {
            return "122";
        } else {
            return "123";
        }
    }
}
