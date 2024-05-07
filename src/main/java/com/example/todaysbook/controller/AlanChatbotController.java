package com.example.todaysbook.controller;

import com.example.todaysbook.service.AlanChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alan")
public class AlanChatbotController {

    private final AlanChatbotService alanChatbotService;

    @GetMapping(value = "/question", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getQuestionResponse(@RequestParam String content) {
        return alanChatbotService.getQuestionResponse(content);
    }


}