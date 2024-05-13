package com.example.todaysbook.controller;

import com.example.todaysbook.domain.dto.CustomUserDetails;
import com.example.todaysbook.domain.entity.User;
import com.example.todaysbook.service.FavoriteBookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(FavoriteBookController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class FavoriteBookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FavoriteBookService favoriteBookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("책 찜하기 컨트롤러 테스트")
    void addFavoriteBookTest() throws Exception {

        //given
        CustomUserDetails userDetails = new CustomUserDetails(User.builder()
                .id(1L).email("test@test.com").password("password").role("ROLE_BRONZE").build());

        String url = "/favorite_book/add";
        Long userId = userDetails.getUserId();
        Long bookId = 1L;

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .param("bookId", String.valueOf(bookId))
                .param("userId", String.valueOf(userId))
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf()));

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("책 찜하기 취소 컨트롤러 테스트")
    void deleteFavoriteBookTest() throws Exception {

        //given
        CustomUserDetails userDetails = new CustomUserDetails(User.builder()
                .id(1L).email("test@test.com").password("password").role("ROLE_BRONZE").build());

        String url = "/favorite_book/delete";
        Long userId = userDetails.getUserId();
        Long bookId = 1L;

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete(url)
                .param("bookId", String.valueOf(bookId))
                .param("userId", String.valueOf(userId))
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf()));

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
