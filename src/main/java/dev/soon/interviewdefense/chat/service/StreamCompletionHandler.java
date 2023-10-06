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

    private final static String DEEP_QUESTION_FLAG = "%deepQ%";
    private final static String GENERAL_QUESTION_FLAG = "%generalQ%";


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        StringBuilder sb = new StringBuilder();
        if(payload.contains(DEEP_QUESTION_FLAG)) {
            String[] payloadSegments = payload.split(DEEP_QUESTION_FLAG);
            Long chatId = Long.parseLong(payloadSegments[0]);
            String userMessage = payloadSegments[1];

            Chat chat = chatRepository.findById(chatId)
                    .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_CHATROOM_IN_DB));
            ChatMessage chatMessageDesc = chatMessageRepository.findTopByChatOrderByCreatedAtDesc(chat)
                    .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_LATEST_CHAT_MESSAGE));

            chatMessageRepository.save(new ChatMessage(userMessage.substring(3), chat, ChatSender.USER));
            Flowable<ChatCompletionChunk> responseFlowable = chatServiceV2.generateStreamResponse(chat, "[" + chatMessageDesc.getMessage() +"]" + "글에서" + userMessage);
            subscribeFlowable(session, chat, sb, responseFlowable);
            return;
        }
        if(payload.contains(GENERAL_QUESTION_FLAG)) {
            String[] payloadSegments = payload.split(GENERAL_QUESTION_FLAG);
            Long chatId = Long.parseLong(payloadSegments[0]);
            String userMessage = payloadSegments[1];
            Chat chat = chatRepository.findById(chatId)
                    .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_CHATROOM_IN_DB));
            chatMessageRepository.save(new ChatMessage(userMessage, chat, ChatSender.USER));
            Flowable<ChatCompletionChunk> responseFlowable = chatServiceV2.generateStreamResponse(chat, userMessage);
            subscribeFlowable(session, chat, sb, responseFlowable);
        }
        else {
            throw new ApiException(CustomErrorCode.INVALID_FLAG_IN_FRONT);
        }
    }

    private void subscribeFlowable(WebSocketSession session, Chat chat, StringBuilder sb, Flowable<ChatCompletionChunk> responseFlowable) {
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
                }
        );
    }
}