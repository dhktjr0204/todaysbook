package com.example.todaysbook.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AlanRecommendApiService {

    private final RestTemplate restTemplate;

    @Value("${alan.api.url}")
    private String apiUrl;

    @Value("${alan.api.client-id}")
    private String clientId;

    private static final Logger logger = LoggerFactory.getLogger(AlanRecommendApiService.class);

    public String callApi() {
        String url = apiUrl + "?content=오늘 기준 한국 도서 베스트셀러 책 제목 15개를 추천해 주세요. 신뢰할 수 있는 최신 정보를 바탕으로 정확한 책 제목을 제공해 주세요. 책 제목만 JSON 형식으로 작성해 주세요. 추가적인 설명이나 마크다운은 포함하지 않도록 해 주세요.&client_id=" + clientId;
        String response = restTemplate.getForObject(url, String.class);
        logger.info("API Response: {}", response);
        return response;
    }
}

    /*
     * TODO: 예외처리하기 (alan ai API 호출이 실패했을때 또는 응답이 예상치 못한 응답을 했을때)
     *  -> 500응답을 하거나
     *  -> 횟수(n번) 정해서 retry 하기
     * */

    /*
     * TODO: client_id 바꾸는 로직 추가하기 (후순위)
     *  -> client_id당 대략 100번 제한을 넘어가면 다음 client_id로 바꿔서 요청하기
     * */

    /*
    * TODO: 프롬프트 수정하기
    *  client_id가 달라도 서로 영향을 받는다.
    *  영향을 받아도 최대한 원하는 데이터 받을수 있도록 하기
    *  ex) 프롬프트: 전 메시지는 완전히 잊고 현재 질문만 답변해주세요.
    * */

    /*
    * TODO: 프롬프트 수정하기 (정확도)
    *  고민해야하는점: 한두개가 정확하지 않다.
    *  ex) "제목": "이달의 책 2024년 4월", (검색해보니 이런 책은 없다. 교보문고 이달의 책 웹페이지임.)
    * */

    /*
     * TODO: 프롬프트 수정하기 (중복)
     *  답변에서 추천하는 책이 날짜가 바뀌어도 같은 책이 추천되는 경우가 있다.
     *  그 경우가 심하면 조정을 해야할 지 고민하기
     * */

    /*
    * TODO: 프롬프트 수정하기 (날짜 자동 없데이트)
    *  프롬프트에서 날짜기준으로 물어보는데 그 날짜를 자동으로 어제 기준으로 업데이트되어서 물어보도록 수정하기
    * */


