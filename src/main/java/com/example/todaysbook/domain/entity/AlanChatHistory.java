package com.example.todaysbook.domain.entity;

import com.example.todaysbook.domain.dto.AlanChatHistoryDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "alan_chat_history")
public class AlanChatHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_input", nullable = false)
    private String userInput;

    @Column(name = "bot_response", nullable = false)
    private String botResponse;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public AlanChatHistory(AlanChatHistoryDto dto) {
        this.userInput = dto.getUserInput();
        this.botResponse = dto.getBotResponse();
        this.createdAt = LocalDateTime.now();
    }
}