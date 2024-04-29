package com.example.todaysbook.domain.entity;

import com.example.todaysbook.domain.dto.AlanRecommendBookDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlanRecommendBook {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String title;


    public AlanRecommendBookDto toDto() {
        return AlanRecommendBookDto.builder()
                .id(id)
                .title(title)
                .build();
    }


}
