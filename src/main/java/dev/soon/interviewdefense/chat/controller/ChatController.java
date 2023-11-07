package dev.soon.interviewdefense.chat.controller;

import dev.soon.interviewdefense.chat.controller.dto.ChatMessageRecordResponse;
import dev.soon.interviewdefense.chat.controller.dto.ChatRequest;
import dev.soon.interviewdefense.chat.controller.dto.ChatResponse;
import dev.soon.interviewdefense.chat.service.ChatService;
import dev.soon.interviewdefense.security.SecurityUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "채팅 관련 API")
public class ChatController {

    private final ChatService chatService;

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "유저 채팅 목록 조회 API")
    @GetMapping
    public List<ChatResponse> getChatsByUser(@Parameter(hidden = true) @AuthenticationPrincipal SecurityUser user) {
        String email = user.getUsername();
        return chatService.getChatsByUser(email);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "유저 채팅 생성 API")
    @PostMapping
    public Long createChat(@Parameter(hidden = true) @AuthenticationPrincipal SecurityUser user,
                           @RequestBody ChatRequest dto) {
        String email = user.getUsername();
        return chatService.createChat(email, dto);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "유저 채팅별 메세지 기록 조회 API")
    @GetMapping("/{chatId}")
    public List<ChatMessageRecordResponse> getChatMessageRecordByChat(@PathVariable Long chatId) {
        return chatService.getChatMessageRecordByChat(chatId);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "유저 채팅 삭제 API")
    @DeleteMapping("/{chatId}")
    public String deleteChat(@Parameter(hidden = true) @AuthenticationPrincipal SecurityUser user,
                             @PathVariable Long chatId) {
        String email = user.getUsername();
        chatService.deleteChat(email, chatId);
        return "deleted";
    }
}