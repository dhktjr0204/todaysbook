package com.example.todaysbook.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlanRecommendList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id; // AlanRecommendList의 id

    private String title; // AlanRecommendList의 제목

    private Timestamp date;



}
