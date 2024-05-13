package com.example.todaysbook.controller;

import com.example.todaysbook.service.SalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Month;
import java.time.Year;

@Controller
@RequestMapping("/sales")
@RequiredArgsConstructor
public class SalesController {

    private final SalesService salesService;

    @GetMapping("")
    public ResponseEntity<?> getSalesByYear(Year year) {

        try {

            return ResponseEntity.ok(salesService.getSalesByYear(year));

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/category")
    public ResponseEntity<?> getSalesByCategory(String year, String month) {

        try {

            return ResponseEntity.ok(salesService.getSalesByCategory(year, month));

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}