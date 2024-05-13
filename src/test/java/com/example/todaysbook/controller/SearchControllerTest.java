package com.example.todaysbook.controller;

import com.example.todaysbook.domain.dto.BookDto;
import com.example.todaysbook.domain.dto.CustomUserDetails;
import com.example.todaysbook.domain.dto.RecommendListDetailWithBookMarkDto;
import com.example.todaysbook.domain.entity.User;
import com.example.todaysbook.service.SearchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@WebMvcTest(SearchController.class)
@MockBean(JpaMetamodelMappingContext.class)
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchService searchService;

    private BookDto createDummyBookDto(){
        return BookDto.builder()
                .id(1L)
                .title("title")
                .author("author")
                .price(1000L)
                .build();
    }

    private RecommendListDetailWithBookMarkDto createDummyRecommendListCreateDto(){
        return RecommendListDetailWithBookMarkDto.builder()
                .bookList(Arrays.asList(
                        BookDto.builder().title("title").author("author").price(1000L).build(),
                        BookDto.builder().title("title").author("author").price(1000L).build()))
                .build();
    }

    @Test
    @DisplayName("검색 테스트")
    @WithMockUser("user1")
    public void SearchTest() throws Exception {
        //given
        String keyword = "book";
        Sort sort = Sort.by(
                Sort.Order.asc("publishDate"),
                Sort.Order.asc("title")
        );
        Pageable pageable = PageRequest.of(0, 10, sort);

        Page<BookDto> result = new PageImpl(Arrays.asList(createDummyBookDto(), createDummyBookDto()), pageable, 2);

        when(searchService.searchBookByKeyword(eq(keyword), any())).thenReturn(result);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/book/search")
                .param("keyword", keyword)
                .with(csrf()));

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("검색어가 빈 값일 때 테스트")
    @WithMockUser("user1")
    public void Search_withNoKeywordTest() throws Exception {
        //given
        String keyword = "";

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/book/search")
                .param("keyword", keyword)
                .with(csrf()));

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.view().name("error/404"));
    }

    @Test
    @DisplayName("리스트 검색 테스트")
    public void SearchListTest() throws Exception {
        //given
        CustomUserDetails userDetails = new CustomUserDetails(User.builder()
                .id(1L).email("test@test.com").password("password").role("ROLE_BRONZE").build());

        String keyword="keyword";
        Long userId=1L;

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        Page<RecommendListDetailWithBookMarkDto> result =
                new PageImpl(Arrays.asList(createDummyRecommendListCreateDto(), createDummyRecommendListCreateDto()), pageable, 2);

        when(searchService.searchListByKeyword(eq(keyword), eq(userId), any())).thenReturn(result);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/book/search/list")
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                .param("keyword", keyword)
                .with(csrf()));

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    @DisplayName("검색어가 없을 때 리스트 검색")
    public void SearchList_withNoKeywordTest() throws Exception {
        //given
        CustomUserDetails userDetails = new CustomUserDetails(User.builder()
                .id(1L).email("test@test.com").password("password").role("ROLE_BRONZE").build());

        String keyword="";

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/book/search/list")
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                .param("keyword", keyword)
                .with(csrf()));

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.view().name("error/404"));
    }

}