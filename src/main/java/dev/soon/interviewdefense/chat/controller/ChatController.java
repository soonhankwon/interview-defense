package dev.soon.interviewdefense.chat.controller;

import dev.soon.interviewdefense.chat.controller.dto.ChatMessageRecordResponse;
import dev.soon.interviewdefense.chat.controller.dto.ChatRequest;
import dev.soon.interviewdefense.chat.controller.dto.ChatResponse;
import dev.soon.interviewdefense.chat.service.ChatService;
import dev.soon.interviewdefense.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatController {

    private final ChatService chatService;

    @ResponseStatus(HttpStatus.CREATED)
    @GetMapping
    public List<ChatResponse> getChatsByUser(@AuthenticationPrincipal SecurityUser user) {
        String email = user.getUsername();
        return chatService.getChatsByUser(email);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public Long createChat(@AuthenticationPrincipal SecurityUser user,
                           @RequestBody ChatRequest dto) {
        String email = user.getUsername();
        return chatService.createChat(email, dto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{chatId}")
    public List<ChatMessageRecordResponse> getChatMessageRecordByChat(@PathVariable Long chatId) {
        return chatService.getChatMessageRecordByChat(chatId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{chatId}")
    public String deleteChat(@AuthenticationPrincipal SecurityUser user,
                                        @PathVariable Long chatId) {
        String email = user.getUsername();
        chatService.deleteChat(email, chatId);
        return "deleted";
    }
}