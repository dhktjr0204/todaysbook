package com.example.todaysbook.controller;

import com.example.todaysbook.domain.dto.BookDto;
import com.example.todaysbook.service.AdminService;
import com.example.todaysbook.service.OrderService;
import com.example.todaysbook.service.RecommendBookService;
import com.example.todaysbook.service.ReviewService;
import com.example.todaysbook.service.SalesService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(AdminController.class)
@MockBean(JpaMetamodelMappingContext.class)
class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RecommendBookService recommendBookService;
    @MockBean
    private ReviewService reviewService;
    @MockBean
    private SalesService salesService;
    @MockBean
    private OrderService orderService;
    @MockBean
    private AdminService adminService;

    private BookDto createDummyBookDto(String title, String author, Long price, String publisher, String description) {
        return BookDto.builder()
                .id(1L)
                .title(title)
                .author(author)
                .price(price)
                .publisher(publisher)
                .description(description)
                .build();
    }

    @Test
    @DisplayName("유저 등급 변경 테스트")
    @WithMockUser(username = "user", roles = "ADMIN")
    public void updateUserRoleTest() throws Exception {
        //given
        Long userId = 1L;
        String role = "ROLE_BRONZE";

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.put("/admin/userlist")
                        .param("userId", String.valueOf(userId))
                        .param("role", role)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("유저 삭제 테스트")
    @WithMockUser(username = "user", roles = "ADMIN")
    public void DeleteUserTest() throws Exception {
        //given
        Long userId = 1L;

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/admin/userlist")
                .param("userId", String.valueOf(userId))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("수량 수정 테스트")
    @WithMockUser(username = "user", roles = "ADMIN")
    public void updateStockTest() throws Exception {
        //given
        Long bookId = 1L;
        Long stock = 20L;

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/admin/stocklist")
                .param("bookId", String.valueOf(bookId))
                .param("stock", String.valueOf(stock))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("책 삭제 테스트")
    @WithMockUser(username = "user", roles = "ADMIN")
    public void deleteStockTest() throws Exception {
        //given
        Long bookId = 1L;

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/admin/stocklist")
                .param("bookId", String.valueOf(bookId))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("책 수정 테스트")
    @WithMockUser(username = "user", roles = "ADMIN")
    public void updateBookTest() throws Exception {
        //given
        BookDto bookDto = createDummyBookDto("title", "author", 1000L, "publisher", "description");

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/admin/booklist/edit")
                .param("title", bookDto.getTitle())
                .param("author", bookDto.getAuthor())
                .param("price", String.valueOf(bookDto.getPrice()))
                .param("publisher", bookDto.getPublisher())
                .param("description", bookDto.getDescription())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("책 제목이 빈값일 때 수정 테스트")
    @WithMockUser(username = "user", roles = "ADMIN")
    public void updateBook_withNoTitleValidTest() throws Exception {
        //given
        BookDto bookDto = createDummyBookDto("", "author", 1000L, "publisher", "description");

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/admin/booklist/edit")
                .param("title", bookDto.getTitle())
                .param("author", bookDto.getAuthor())
                .param("price", String.valueOf(bookDto.getPrice()))
                .param("publisher", bookDto.getPublisher())
                .param("description", bookDto.getDescription())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedMessage = "제목을 입력해주세요.";
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));;
    }

    @Test
    @DisplayName("책 제목이 최대 길이를 초과했을 때 수정 테스트")
    @WithMockUser(username = "user", roles = "ADMIN")
    public void updateBook_withTitleLengthValidTest() throws Exception {
        //given
        BookDto bookDto = createDummyBookDto("titletitle".repeat(30), "author", 1000L, "publisher", "description");

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/admin/booklist/edit")
                .param("title", bookDto.getTitle())
                .param("author", bookDto.getAuthor())
                .param("price", String.valueOf(bookDto.getPrice()))
                .param("publisher", bookDto.getPublisher())
                .param("description", bookDto.getDescription())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedMessage = "제목 길이가 초과하였습니다.";
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));;
    }

    @Test
    @DisplayName("책 작가가 최대 길이를 초과했을 때 수정 테스트")
    @WithMockUser(username = "user", roles = "ADMIN")
    public void updateBook_withAuthorLengthValidTest() throws Exception {
        //given
        BookDto bookDto = createDummyBookDto("title", "authorauthor".repeat(30), 1000L, "publisher", "description");

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/admin/booklist/edit")
                .param("title", bookDto.getTitle())
                .param("author", bookDto.getAuthor())
                .param("price", String.valueOf(bookDto.getPrice()))
                .param("publisher", bookDto.getPublisher())
                .param("description", bookDto.getDescription())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedMessage = "작가 길이가 초과되었습니다.";
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));;
    }

    @Test
    @DisplayName("책 작가가 빈값일 때 수정 테스트")
    @WithMockUser(username = "user", roles = "ADMIN")
    public void updateBook_withNoAuthorValidTest() throws Exception {
        //given
        BookDto bookDto = createDummyBookDto("title", "", 1000L, "publisher", "description");

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/admin/booklist/edit")
                .param("title", bookDto.getTitle())
                .param("author", bookDto.getAuthor())
                .param("price", String.valueOf(bookDto.getPrice()))
                .param("publisher", bookDto.getPublisher())
                .param("description", bookDto.getDescription())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedMessage = "작가 이름을 입력해주세요.";
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));;
    }

    @Test
    @DisplayName("책 가격이 빈값일 때 수정 테스트")
    @WithMockUser(username = "user", roles = "ADMIN")
    public void updateBook_withNoPriceValidTest() throws Exception {
        //given
        BookDto bookDto = createDummyBookDto("title", "author", null, "publisher", "description");

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/admin/booklist/edit")
                .param("title", bookDto.getTitle())
                .param("author", bookDto.getAuthor())
                .param("price", String.valueOf(bookDto.getPrice()))
                .param("publisher", bookDto.getPublisher())
                .param("description", bookDto.getDescription())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedMessage = "책 가격을 입력해주세요.";
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));;
    }

    @Test
    @DisplayName("책 출판사가 빈값일 때 수정 테스트")
    @WithMockUser(username = "user", roles = "ADMIN")
    public void updateBook_withNoPublisherValidTest() throws Exception {
        //given
        BookDto bookDto = createDummyBookDto("title", "author", 1000L, "", "description");

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/admin/booklist/edit")
                .param("title", bookDto.getTitle())
                .param("author", bookDto.getAuthor())
                .param("price", String.valueOf(bookDto.getPrice()))
                .param("publisher", bookDto.getPublisher())
                .param("description", bookDto.getDescription())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedMessage = "출판사를 입력해주세요.";
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));;
    }

    @Test
    @DisplayName("책 출판사가 최대 길이를 초과할때  수정 테스트")
    @WithMockUser(username = "user", roles = "ADMIN")
    public void updateBook_withPublisherLengthValidTest() throws Exception {
        //given
        BookDto bookDto = createDummyBookDto("title", "author", 1000L, "publisher".repeat(5), "description");

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/admin/booklist/edit")
                .param("title", bookDto.getTitle())
                .param("author", bookDto.getAuthor())
                .param("price", String.valueOf(bookDto.getPrice()))
                .param("publisher", bookDto.getPublisher())
                .param("description", bookDto.getDescription())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedMessage = "출판사 길이가 초과되었습니다.";
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));;
    }

    @Test
    @DisplayName("책 설명이 최대 길이를 초과할때  수정 테스트")
    @WithMockUser(username = "user", roles = "ADMIN")
    public void updateBook_withDescriptionLengthValidTest() throws Exception {
        //given
        BookDto bookDto = createDummyBookDto("title", "author", 1000L, "publisher", "descriptiondescription".repeat(50));

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/admin/booklist/edit")
                .param("title", bookDto.getTitle())
                .param("author", bookDto.getAuthor())
                .param("price", String.valueOf(bookDto.getPrice()))
                .param("publisher", bookDto.getPublisher())
                .param("description", bookDto.getDescription())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedMessage = "책 설명의 길이가 초과되었습니다.";
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));;
    }
}