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
        StringBuilder chunkBuffer = new StringBuilder();
        ObjectMapper objectMapper = new ObjectMapper();
        responseFlowable.subscribe(
                chunk -> {
                    try {
                        String response = chunk.getChoices().get(0).getMessage().getContent();
                        log.info("response={}", response);
                        if(response != null) {
                            chunkBuffer.append(response);
                            sb.append(response);
                            if(chunkBuffer.toString().length() >= 5) {
                                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(chunkBuffer.toString())));
                                chunkBuffer.setLength(0);
                                return;
                            }
                            return;
                        }
                        if(chunkBuffer.length() > 0) {
                            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(chunkBuffer.toString())));
                            chunkBuffer.setLength(0);
                        }
                        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(null)));
                        chatMessageRepository.save(new ChatMessage(sb.toString(), chat, ChatSender.AI));
                        sb.setLength(0);
                    } catch (Exception e) {
                        log.error("An error occurred while processing the flowable", e);
                    }
                },
                Throwable::printStackTrace
        );
    }
}