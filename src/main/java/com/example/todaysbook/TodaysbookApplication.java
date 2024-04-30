package com.example.todaysbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableJpaAuditing
@EnableScheduling // alan 오늘의 책 추천: 스케줄러 기능 활성화
@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
public class TodaysbookApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodaysbookApplication.class, args);
    }

}
