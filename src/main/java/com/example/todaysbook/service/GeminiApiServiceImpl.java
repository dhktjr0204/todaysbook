package com.example.todaysbook.service;

import com.example.todaysbook.constant.Constant;
import com.example.todaysbook.domain.dto.GeminiRecommendApiRequest;
import com.example.todaysbook.domain.dto.GeminiRecommendApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class GeminiApiServiceImpl implements GeminiApiService {
    @Qualifier("geminiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.generationConfig.candidate_count}")
    private int candidateCount;

    @Value("${gemini.generationConfig.max_output_tokens}")
    private int maxOutputTokens;

    private final GeminiRecommendBookService geminiRecommendBookService;

    // 자동으로 책 추천
    public ResponseEntity<String> AutomaticallycallGeminiApi() {
        String prompt = String.format(Constant.DEFAULT_PROMPT, Constant.DEFAULT_NATION, Constant.DEFAULT_QUANTITY);
        return callGeminiApi(prompt, Constant.DEFAULT_QUANTITY, Constant.DEFAULT_TEMPERATURE);
    }

    // 수동으로 책 추천
    public void  ManuallyCallGeminiApi(Integer quantity, Double temperature) throws UnsupportedEncodingException {
        quantity = quantity != null ? quantity : Constant.DEFAULT_QUANTITY;
        temperature = temperature != null ? temperature : Constant.DEFAULT_TEMPERATURE;

        String prompt = String.format(Constant.DEFAULT_PROMPT, Constant.DEFAULT_NATION, quantity);
        callGeminiApi(prompt, quantity, temperature);
    }

    // Gemini API 호출
    private ResponseEntity<String> callGeminiApi(String prompt, int quantity, double temperature) {
        try {
            String message = getContents(prompt, temperature);
            geminiRecommendBookService.saveGeminiRecommendBook(message);
            return ResponseEntity.ok(message);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    // Gemini API 응답
    private String getContents(String prompt, double temperature) throws UnsupportedEncodingException {
        String requestUrl = apiUrl + "?key=" + geminiApiKey;
        GeminiRecommendApiRequest request = new GeminiRecommendApiRequest(prompt, candidateCount, maxOutputTokens, temperature);
        GeminiRecommendApiResponse response = restTemplate.postForObject(requestUrl, request, GeminiRecommendApiResponse.class);
        return response.getCandidates().get(0).getContent().getParts().get(0).getText();
    }

}