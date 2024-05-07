package com.example.todaysbook.config;// GeminiRestTemplateConfig.java

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class GeminiRestTemplateConfig {

    @Bean
    @Qualifier("geminiRestTemplate")
    public RestTemplate geminiRestTemplate() { //Gemini API를 호출하기 위한 RestTemplate 빈을 생성
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> execution.execute(request, body));

        return restTemplate;
    }
}