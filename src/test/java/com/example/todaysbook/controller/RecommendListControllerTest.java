package com.example.todaysbook.controller;

import com.example.todaysbook.domain.dto.CustomUserDetails;
import com.example.todaysbook.domain.dto.RecommendListCreateRequestDto;
import com.example.todaysbook.domain.dto.RecommendListUpdateRequestDto;
import com.example.todaysbook.domain.entity.User;
import com.example.todaysbook.service.RecommendListService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(RecommendListController.class)
@MockBean(JpaMetamodelMappingContext.class)
class RecommendListControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecommendListService recommendListService;

    @MockBean
    private UserDetailsService userService;


    private RecommendListCreateRequestDto createDummyRecommendListCreateDto(String title, List<Long> bookList) {
        return RecommendListCreateRequestDto.builder()
                .title(title)
                .bookIdList(bookList)
                .build();
    }

    private RecommendListUpdateRequestDto createDummyRecommendListUpdateDto(Long userId, String title, List<Long> booklist) {
        return RecommendListUpdateRequestDto.builder()
                .userId(userId)
                .title(title)
                .bookIdList(booklist)
                .build();
    }

    @Test
    @DisplayName("저장 테스트")
    void saveList() throws Exception {
        //given
        CustomUserDetails userDetails = new CustomUserDetails(User.builder()
                .id(1L).email("test@test.com").password("password").role("ROLE_BRONZE").build());

        RecommendListCreateRequestDto request =
                createDummyRecommendListCreateDto("title", Arrays.asList(1L, 2L, 3L));

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/recommend/add")
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                .param("title", request.getTitle())
                .param("bookIdList", String.valueOf(request.getBookIdList().get(0)))
                .param("bookIdList", String.valueOf(request.getBookIdList().get(1)))
                .param("bookIdList", String.valueOf(request.getBookIdList().get(2)))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .with(csrf()));

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("리스트 제목 길이가 길 때 저장 테스트")
    public void save_withTitleLengthValidTest() throws Exception {
        //given
        CustomUserDetails userDetails = new CustomUserDetails(User.builder()
                .id(1L).email("test@test.com").password("password").role("ROLE_BRONZE").build());

        RecommendListCreateRequestDto request =
                createDummyRecommendListCreateDto("title".repeat(10), Arrays.asList(1L, 2L, 3L));

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/recommend/add")
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                .param("title", request.getTitle())
                .param("bookIdList", String.valueOf(request.getBookIdList().get(0)))
                .param("bookIdList", String.valueOf(request.getBookIdList().get(1)))
                .param("bookIdList", String.valueOf(request.getBookIdList().get(2)))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .with(csrf()));

        //then
        String expectedMessage = "제목 길이가 초과하였습니다.";
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));

    }

    @Test
    @DisplayName("리스트 제목을 입력하지 않았을 때 테스트")
    public void save_withNoTitleValidTest() throws Exception {
        //given
        CustomUserDetails userDetails = new CustomUserDetails(User.builder()
                .id(1L).email("test@test.com").password("password").role("ROLE_BRONZE").build());

        RecommendListCreateRequestDto request =
                createDummyRecommendListCreateDto("", Arrays.asList(1L, 2L, 3L));

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/recommend/add")
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                .param("title", request.getTitle())
                .param("bookIdList", String.valueOf(request.getBookIdList().get(0)))
                .param("bookIdList", String.valueOf(request.getBookIdList().get(1)))
                .param("bookIdList", String.valueOf(request.getBookIdList().get(2)))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .with(csrf()));

        //then
        String expectedMessage = "제목을 입력해주세요.";
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));

    }

    @Test
    @DisplayName("리스트에 책 개수가 10개가 넘을 때 테스트")
    public void save_withBookSizeValidTest() throws Exception {
        //given
        CustomUserDetails userDetails = new CustomUserDetails(User.builder()
                .id(1L).email("test@test.com").password("password").role("ROLE_BRONZE").build());

        RecommendListCreateRequestDto request =
                createDummyRecommendListCreateDto("title", Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L));

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/recommend/add")
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                .param("title", request.getTitle())
                .param("bookIdList", String.valueOf(request.getBookIdList().get(0)))
                .param("bookIdList", String.valueOf(request.getBookIdList().get(1)))
                .param("bookIdList", String.valueOf(request.getBookIdList().get(2)))
                .param("bookIdList", String.valueOf(request.getBookIdList().get(3)))
                .param("bookIdList", String.valueOf(request.getBookIdList().get(4)))
                .param("bookIdList", String.valueOf(request.getBookIdList().get(5)))
                .param("bookIdList", String.valueOf(request.getBookIdList().get(6)))
                .param("bookIdList", String.valueOf(request.getBookIdList().get(7)))
                .param("bookIdList", String.valueOf(request.getBookIdList().get(8)))
                .param("bookIdList", String.valueOf(request.getBookIdList().get(9)))
                .param("bookIdList", String.valueOf(request.getBookIdList().get(10)))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .with(csrf()));

        //then
        String expectedMessage = "추천 리스트는 10개 이하로 구성해주세요.";
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
    }

    @Test
    @DisplayName("수정 테스트")
    public void updateListTest() throws Exception {
        //given
        Long listId = 1L;
        Long userId = 1L;

        CustomUserDetails userDetails = new CustomUserDetails(User.builder()
                .id(1L).email("test@test.com").password("password").role("ROLE_BRONZE").build());

        RecommendListUpdateRequestDto request = createDummyRecommendListUpdateDto(userId, "title-test", Arrays.asList(1L, 2L, 3L));

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/recommend/update/" + listId)
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                .param("userId", String.valueOf(request.getUserId()))
                .param("title", request.getTitle())
                .param("bookIdList", String.valueOf(request.getBookIdList().get(0)))
                .param("bookIdList", String.valueOf(request.getBookIdList().get(1)))
                .param("bookIdList", String.valueOf(request.getBookIdList().get(2)))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .with(csrf()));

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("리스트 제목 길이 넘을 때 수정 테스트")
    public void updateList_withTitleLengthValidTest() throws Exception {
        //given
        Long listId = 1L;
        Long userId = 1L;

        CustomUserDetails userDetails = new CustomUserDetails(User.builder()
                .id(1L).email("test@test.com").password("password").role("ROLE_BRONZE").build());

        RecommendListUpdateRequestDto request =
                createDummyRecommendListUpdateDto(userId, "title-test".repeat(10), Arrays.asList(1L, 2L, 3L));

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/recommend/update/" + listId)
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                .param("userId", String.valueOf(request.getUserId()))
                .param("title", request.getTitle())
                .param("bookIdList", String.valueOf(request.getBookIdList().get(0)))
                .param("bookIdList", String.valueOf(request.getBookIdList().get(1)))
                .param("bookIdList", String.valueOf(request.getBookIdList().get(2)))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .with(csrf()));

        //then
        String expectedMessage = "제목 길이가 초과하였습니다.";
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
    }

    @Test
    @DisplayName("리스트 제목이 빈값일 때 테스트")
    public void updateList_withNoTitleValidTest() throws Exception {
        //given
        Long listId = 1L;
        Long userId = 1L;

        CustomUserDetails userDetails = new CustomUserDetails(User.builder()
                .id(1L).email("test@test.com").password("password").role("ROLE_BRONZE").build());

        RecommendListUpdateRequestDto request =
                createDummyRecommendListUpdateDto(userId, "", Arrays.asList(1L, 2L, 3L));

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/recommend/update/" + listId)
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                .param("userId", String.valueOf(request.getUserId()))
                .param("title", request.getTitle())
                .param("bookIdList", String.valueOf(request.getBookIdList().get(0)))
                .param("bookIdList", String.valueOf(request.getBookIdList().get(1)))
                .param("bookIdList", String.valueOf(request.getBookIdList().get(2)))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .with(csrf()));

        //then
        String expectedMessage = "제목을 입력해주세요.";
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
    }


    @Test
    @DisplayName("삭제 테스트")
    public void deleteListTest() throws Exception {
        //given
        Long listId = 1L;
        Long userId = 1L;

        CustomUserDetails userDetails = new CustomUserDetails(User.builder()
                .id(1L).email("test@test.com").password("password").role("ROLE_BRONZE").build());

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/recommend/remove/" + listId)
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                .param("userId", String.valueOf(userId))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .with(csrf()));

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("리스트 만든 유저와 요청한 유저가 다를 때 삭제 테스트")
    public void deleteList_withUserValidTest() throws Exception {
        //given
        Long listId = 1L;
        Long userId = 1L;

        CustomUserDetails userDetails = new CustomUserDetails(User.builder()
                .id(2L).email("test@test.com").password("password").role("ROLE_BRONZE").build());

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/recommend/remove/" + listId)
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                .param("userId", String.valueOf(userId))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .with(csrf()));

        //then
        String expectedMessage = "인증되지 않은 유저입니다.";
        resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
    }
}
