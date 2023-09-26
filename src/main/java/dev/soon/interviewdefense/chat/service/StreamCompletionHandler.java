package dev.soon.interviewdefense.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import dev.soon.interviewdefense.chat.controller.dto.GPTCompletionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class StreamCompletionHandler extends TextWebSocketHandler {

    private final HashMap<String, WebSocketSession> sessionHashMap;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OpenAiService openAiService;

    // Client 접속시 호출되는 메서드
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessionHashMap.put(session.getId(), session);
        log.info("connected user={}", session.getId());
    }

    // Client 접속 해제시 호출되는 메서드
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionHashMap.remove(session.getId());
        log.info("disconnected user={}", session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        GPTCompletionRequest completionRequest = objectMapper.readValue(message.getPayload(), GPTCompletionRequest.class);
        sessionHashMap.keySet().forEach(key -> {
            streamChatCompletion(key, completionRequest);
        });
    }

    private void streamChatCompletion(String key, GPTCompletionRequest request) {
        openAiService.streamChatCompletion(requestOpenAI(new ChatMessage("system", "프로그래밍 전문가"), request.getPrompt()))
                .blockingForEach(completionChunk -> {
                    sessionHashMap.get(key).sendMessage(new TextMessage(objectMapper.writeValueAsString(completionChunk)));
                });
    }

    private ChatCompletionRequest requestOpenAI(com.theokanning.openai.completion.chat.ChatMessage rolePrompt, String userPrompt) {
        return ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(List.of(
                        rolePrompt,
                        new com.theokanning.openai.completion.chat.ChatMessage("user", userPrompt)
                ))
                .maxTokens(300)
                .build();
    }
}