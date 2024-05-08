package com.example.todaysbook.controller;

        import com.example.todaysbook.domain.dto.AlanChatApiRequest;
        import com.example.todaysbook.domain.dto.AlanChatApiResponse;
        import com.example.todaysbook.domain.dto.AlanChatHistoryDto;
        import com.example.todaysbook.domain.entity.AlanChatHistory;

        import com.example.todaysbook.repository.AlanChatHistoryRepository;
        import com.example.todaysbook.service.AlanChatService;
        import lombok.RequiredArgsConstructor;
        import org.springframework.stereotype.Controller;
        import org.springframework.ui.Model;
        import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/alan")
@Controller
public class AlanChatController {

    private final AlanChatService alanChatService;
    private final AlanChatHistoryRepository alanChatHistoryRepository;

    @GetMapping("/chat")
    public String chatbotPage(Model model) {
        model.addAttribute("chatHistory", alanChatHistoryRepository.findAll());
        return "alan/chat";
    }

    @PostMapping("/chat")
    public String handleUserInput(@RequestBody AlanChatApiRequest request, Model model) {
        // Alan AI API 호출
        AlanChatApiResponse response = alanChatService.getResponse(request);

        // 대화 내역 저장
        AlanChatHistoryDto chatHistoryDto = new AlanChatHistoryDto();
        chatHistoryDto.setUserInput(request.getContent());
        chatHistoryDto.setBotResponse(response.getContent());
        AlanChatHistory alanChatHistory = new AlanChatHistory(chatHistoryDto);
        alanChatHistoryRepository.save(alanChatHistory);

        model.addAttribute("chatHistory", alanChatHistoryRepository.findAll());
        return "alan/chat";
    }
}