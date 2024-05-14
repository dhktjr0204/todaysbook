package com.example.todaysbook.controller;

import com.example.todaysbook.domain.dto.AlanChatResponse;
import com.example.todaysbook.service.AlanChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@RequestMapping("/alan")
@RestController
public class AlanChatController {

    private final AlanChatService alanChatService;

    @GetMapping("/sse-streaming")
    public Flux<ServerSentEvent<AlanChatResponse>> streamAlanResponse(@RequestParam("content") String content) {
        return alanChatService.streamAlanResponse(content);
    }
    @GetMapping("/reset-state")
    public void resetState() {
        alanChatService.resetState();
    }
}
