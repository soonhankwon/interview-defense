package dev.soon.interviewdefense.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import dev.soon.interviewdefense.chat.domain.Chat;
import dev.soon.interviewdefense.chat.domain.ChatMessage;
import dev.soon.interviewdefense.chat.domain.ChatSender;
import dev.soon.interviewdefense.chat.respository.ChatMessageRepository;
import dev.soon.interviewdefense.chat.respository.ChatRepository;
import dev.soon.interviewdefense.exception.ApiException;
import dev.soon.interviewdefense.exception.CustomErrorCode;
import io.reactivex.Flowable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.security.Principal;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class StreamCompletionHandler extends TextWebSocketHandler {

    private final Map<String, Chat> map = new ConcurrentHashMap<>();
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRepository chatRepository;
    private final ChatService chatServiceV2;

    private final static String DEEP_QUESTION_FLAG = "%deepQ%";
    private final static String START_CHAT_FLAG = "%start%";

    private static long start;

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Principal principal = session.getPrincipal();
        if (Objects.requireNonNull(principal).getName() != null) {
            map.remove(principal.getName());
        }
        session.close();
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        start = System.currentTimeMillis();
        log.info("start logic={}", start);
        String payload = message.getPayload();
        String email = Objects.requireNonNull(session.getPrincipal()).getName();
        if(hasStartFlag(payload)) {
            saveChatInMap(payload, email);
            return;
        }
        StringBuilder sb = new StringBuilder();
        if (hasDeepFlag(payload)) {
            String userMessage = payload.replaceAll(DEEP_QUESTION_FLAG, "");
            Chat chat = map.get(email);
            ChatMessage chatMessageDesc = chatMessageRepository.findTopByChatOrderByCreatedAtDesc(chat)
                    .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_LATEST_CHAT_MESSAGE));

            chatMessageRepository.save(new ChatMessage(userMessage.substring(3), chat, ChatSender.USER));
            Flowable<ChatCompletionChunk> responseFlowable = chatServiceV2.generateStreamResponse(chat, "[" + chatMessageDesc.getMessage() + "]" + "글에서" + userMessage);
            subscribeFlowable(session, chat, sb, responseFlowable);
        } else {
            Chat chat = map.get(email);
            chatMessageRepository.save(new ChatMessage(payload, chat, ChatSender.USER));
            Flowable<ChatCompletionChunk> responseFlowable = chatServiceV2.generateStreamResponse(chat, payload);
            subscribeFlowable(session, chat, sb, responseFlowable);
        }
    }

    private boolean hasStartFlag(String payload) {
        return payload.contains(START_CHAT_FLAG);
    }

    private void saveChatInMap(String payload, String email) {
        Long chatId = Long.parseLong(payload.replaceAll(START_CHAT_FLAG, ""));
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_CHATROOM_IN_DB));
        map.put(email, chat);
    }

    private boolean hasDeepFlag(String payload) {
        return payload.contains(DEEP_QUESTION_FLAG);
    }

    private void subscribeFlowable(WebSocketSession session, Chat chat, StringBuilder sb, Flowable<ChatCompletionChunk> responseFlowable) {
        StringBuilder chunkBuffer = new StringBuilder();
        ObjectMapper objectMapper = new ObjectMapper();
        responseFlowable.subscribe(
                chunk -> {
                    try {
                        String response = chunk.getChoices().get(0).getMessage().getContent();
                        log.info("response={}", response);
                        if (response != null) {
                            chunkBuffer.append(response);
                            sb.append(response);
                            if (chunkBuffer.toString().length() >= 5) {
                                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(chunkBuffer.toString())));
                                chunkBuffer.setLength(0);
                                return;
                            }
                            return;
                        }
                        if (chunkBuffer.length() > 0) {
                            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(chunkBuffer.toString())));
                            chunkBuffer.setLength(0);
                        }
                        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(null)));
                        chatMessageRepository.save(new ChatMessage(sb.toString(), chat, ChatSender.AI));
                        sb.setLength(0);
                        long end = System.currentTimeMillis();
                        log.info("spend time={}", end - start);
                    } catch (Exception e) {
                        log.error("An error occurred while processing the flowable", e);
                    }
                },
                Throwable::printStackTrace
        );
    }
}