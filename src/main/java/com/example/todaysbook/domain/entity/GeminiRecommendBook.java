package com.example.todaysbook.domain.entity;

import com.example.todaysbook.domain.dto.GeminiRecommendBookDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class GeminiRecommendBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookId;

    @CreatedDate
    private LocalDateTime date;

    public GeminiRecommendBookDto toDto() {
        return GeminiRecommendBookDto.builder()
                .id(id)
                .bookId(bookId)
                .date(date)
                .build();
    }
}
