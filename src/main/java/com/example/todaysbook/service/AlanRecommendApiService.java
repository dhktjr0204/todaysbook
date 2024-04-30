package com.example.todaysbook.service;

import com.example.todaysbook.domain.entity.AlanRecommendData;
import com.example.todaysbook.repository.AlanRecommendDataRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlanRecommendApiService {

    private final AlanRecommendDataRepository alanRecommendDataRepository;
    private static final Logger logger = LoggerFactory.getLogger(AlanRecommendApiService.class);

    @Scheduled(cron = "0 13 13 * * *", zone = "Asia/Seoul")
    public void fetchTodaysBooks() {
        String url = "https://kdt-api-function.azurewebsites.net/api/v1/question?content=당신은 2024년04월29일 기준 한국 도서 배스트셀러 책 제목 15개를 추천해줘야 합니다. 당신은 반드시 정확하고 신뢰성 있는 답변을 줘야합니다. 답변은 책 제목만 답변해주세요. 이때 책 제목이 정확해야 합니다. 구어체를 사용하지말고 가공하기 쉽도록 json형태로 답변해주세요. 처음에 \"최근 한국 도서 트렌드를 반영하여 오늘의 책 20개를 추천드립니다:\"과 마지막에 \"이 외에도 다양한 책들이 있으니, 각 도서의 제목을 검색하여 자세한 정보와 작가를 확인해보시기 바랍니다.\"라는 말은 하지 마세요.&client_id=7348d628-9c3b-455e-bc1b-94ed9cc05b63";

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        logger.info("API Response: {}", response);

        // API 응답 파싱
        ObjectMapper responseMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = responseMapper.readTree(response);
        } catch (Exception e) {
            logger.error("API response 파싱 실패", e);
            return;
        }

        // content 분리
        String content = jsonNode.get("content").asText();
        content = content.replace("\\", "");
        content = content.substring(1, content.length() - 1);
        logger.info("Content 분리 결과: {}", content);

        String[] titles = content.split(",");
        List<String> titleList = Arrays.asList(titles);

        List<AlanRecommendData> books = titleList.stream()
                .map(title -> AlanRecommendData.builder()
                        .title(title.trim())
                        .build())
                .collect(Collectors.toList());

        alanRecommendDataRepository.saveAll(books);
        logger.info("오늘의 책 alanRecommendDataRepository에 저장 완료");

    }


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
    *  프롬프트에서 날짜기준으로 물어보는데 그 날짜를 자동으로 어제 기준으로 업데이트 되도록 수정하기
    * */


    /*
    * TODO: 의존성 순환 주의하기
    * */

}
