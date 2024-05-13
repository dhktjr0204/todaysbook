package com.example.todaysbook.controller;

import com.example.todaysbook.domain.dto.CustomUserDetails;
import com.example.todaysbook.domain.dto.MyReview;
import com.example.todaysbook.domain.dto.Review;
import com.example.todaysbook.domain.dto.ReviewRequestDto;
import com.example.todaysbook.domain.entity.User;
import com.example.todaysbook.repository.ReviewMapper;
import com.example.todaysbook.repository.ReviewRepository;
import com.example.todaysbook.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@WebMvcTest(ReviewController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private ReviewMapper reviewMapper;
    @MockBean
    private ReviewService reviewService;

    private ReviewRequestDto createDummy(String content) {

        return ReviewRequestDto.builder()
                .reviewId(1L)
                .bookId(1L)
                .content(content)
                .score(5)
                .userId(1L)
                .build();
    }

    @Test
    @DisplayName("리뷰 등록 테스트")
    void addReview() throws Exception {

        //given
        CustomUserDetails userDetails = new CustomUserDetails(User.builder()
                .id(1L).email("test@test.com").password("password").role("ROLE_BRONZE").build());

        String url = "/review/add";
        ReviewRequestDto request = createDummy("test");

        String requestBody = objectMapper.writeValueAsString(request);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody)
                .with(csrf()));

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("리뷰 내용 길이 위반 테스트")
    void addReviewWithContentOverLength() throws Exception {

        //given
        CustomUserDetails userDetails = new CustomUserDetails(User.builder()
                .id(1L).email("test@test.com").password("password").role("ROLE_BRONZE").build());

        String url = "/review/add";
        ReviewRequestDto request = createDummy("testtesttest".repeat(50));

        String requestBody = objectMapper.writeValueAsString(request);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody)
                .with(csrf()));

        //then
        String expectedMessage = "리뷰 내용의 길이가 초과되었습니다.";
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
    }

    @Test
    @DisplayName("리뷰 내용 없음 테스트")
    void addReviewWithNoContent() throws Exception {

        //given
        CustomUserDetails userDetails = new CustomUserDetails(User.builder()
                .id(1L).email("test@test.com").password("password").role("ROLE_BRONZE").build());

        String url = "/review/add";
        ReviewRequestDto request = createDummy("");

        String requestBody = objectMapper.writeValueAsString(request);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody)
                .with(csrf()));

        //then
        String expectedMessage = "리뷰 내용을 입력해주세요.";
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
    }
}
