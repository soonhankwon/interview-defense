package dev.soon.interviewdefense.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import dev.soon.interviewdefense.chat.domain.Chat;
import dev.soon.interviewdefense.chat.domain.ChatMessage;
import dev.soon.interviewdefense.chat.domain.ChatSender;
import dev.soon.interviewdefense.chat.respository.ChatMessageRepository;
import dev.soon.interviewdefense.chat.respository.ChatRepository;
import io.reactivex.Flowable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class StreamCompletionHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRepository chatRepository;
    private final ChatService chatServiceV2;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        String[] payloadSegments = payload.split("%flag%");
        Long chatId = Long.parseLong(payloadSegments[0]);
        String userMessage = payloadSegments[1];
        Chat chat = chatRepository.findById(chatId).orElseThrow();
        chatMessageRepository.save(new ChatMessage(userMessage, chat, ChatSender.USER));

        StringBuilder sb = new StringBuilder();
        Flowable<ChatCompletionChunk> responseFlowable = chatServiceV2.generateStreamResponse(chat, userMessage);

        // Flowable 을 구독하여 응답 스트림을 WebSocket 으로 전송
        responseFlowable.subscribe(
                chunk -> {
                    try {
                        String response = chunk.getChoices().get(0).getMessage().getContent();
                        log.info("response={}", response);
                        if(response == null) {
                            return;
                        }
                        sb.append(response);
                        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace,
                () -> {
                    chatMessageRepository.save(new ChatMessage(sb.toString(), chat, ChatSender.AI));
                    sb.setLength(0);
                    // 응답 스트림이 완료될 때 실행할 코드
                    // 여기에서 스트림 종료를 처리하거나 추가 작업을 수행할 수 있습니다.
                }
        );
    }
}