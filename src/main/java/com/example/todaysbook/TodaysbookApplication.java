package com.example.todaysbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
<<<<<<< HEAD
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // alan 오늘의 책 추천: 스케줄러 기능 활성화
=======
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//@SpringBootApplication
@EnableJpaAuditing
@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
>>>>>>> master
public class TodaysbookApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodaysbookApplication.class, args);
    }

}
