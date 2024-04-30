package com.example.todaysbook.domain.entity;

import com.example.todaysbook.domain.dto.AlanRecommendDataDto;
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



@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlanRecommendData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String title;

    @CreatedDate
    private LocalDateTime createdAt; // db에 저장된 날짜시간
    // saveAlanRecommendBooks()에서 db에 있는지 판별할 때 성능감소 위해 추가

    public AlanRecommendDataDto toDto() {
        return AlanRecommendDataDto.builder()
                .id(id)
                .title(title)
                .createdAt(createdAt)
                .build();
    }
}
