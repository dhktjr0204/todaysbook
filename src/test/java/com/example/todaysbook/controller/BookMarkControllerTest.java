package com.example.todaysbook.controller;

import com.example.todaysbook.domain.dto.CustomUserDetails;
import com.example.todaysbook.domain.entity.User;
import com.example.todaysbook.service.BookMarkService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(BookMarkController.class)
@MockBean(JpaMetamodelMappingContext.class)
class BookMarkControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private BookMarkService bookMarkService;

    @Test
    @DisplayName("북마크 추가 테스트")
    public void addBookMarkTest() throws Exception {
        //given
        Long listId=1L;

        CustomUserDetails userDetails = new CustomUserDetails(User.builder()
                .id(1L).email("test@test.com").password("password").role("ROLE_BRONZE").build());

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/bookmark/add")
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                        .param("listId", String.valueOf(listId))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("로그인 안한 유저가 북마크 추가했을 때 테스트")
    @WithMockUser("user")
    public void addBookMark_withNotLoggedInUserTest() throws Exception {
        //given
        Long listId=1L;

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/bookmark/add")
                .param("listId", String.valueOf(listId))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedMessage = "로그인을 해주세요.";
        resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
    }

    @Test
    @DisplayName("북마크 삭제 테스트")
    public void cancelBookMarkTest() throws Exception {
        //given
        Long listId=1L;

        CustomUserDetails userDetails = new CustomUserDetails(User.builder()
                .id(1L).email("test@test.com").password("password").role("ROLE_BRONZE").build());

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/bookmark/cancel")
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                        .param("listId", String.valueOf(listId))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("로그인 안한 유저가 북마크 추가했을 때 테스트")
    @WithMockUser("user")
    public void cancelBookMark_withNotLoggedInUserTest() throws Exception {
        //given
        Long listId=1L;

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/bookmark/cancel")
                .param("listId", String.valueOf(listId))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedMessage = "로그인을 해주세요.";
        resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
    }
}