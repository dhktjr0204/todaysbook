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


    public AlanRecommendDataDto toDto() {
        return AlanRecommendDataDto.builder()
                .id(id)
                .title(title)
                .build();
    }
}
