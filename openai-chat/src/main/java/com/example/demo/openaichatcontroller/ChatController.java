package com.example.demo.openaichatcontroller;

import com.example.demo.openaichatdto.ChatRequest;
import com.example.demo.openaichatdto.ChatResponse;
import com.example.demo.openaichatservice.ChatService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {

        if (request.message() == null
                || request.message().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Message cannot be empty"
            );
        }

        String reply = chatService.chat(request.message());

        return new ChatResponse(reply);
    }
}


