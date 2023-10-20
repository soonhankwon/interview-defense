package dev.soon.interviewdefense.chat.service;

import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import dev.soon.interviewdefense.chat.controller.dto.ChatRoomReqDto;
import dev.soon.interviewdefense.chat.domain.Chat;
import dev.soon.interviewdefense.chat.domain.ChatMessage;
import io.reactivex.Flowable;

import java.util.List;

public interface ChatService {
    Chat getChatRoom(String email, Long chatRoomId);

    Long createChatRoom(String email, ChatRoomReqDto dto);

    Flowable<ChatCompletionChunk> generateStreamResponse(Chat chat, String question);

    void deleteChat(Long chatRoomId, String email);

    List<ChatMessage> getChatRoomMessages(Chat chatRoom);
}
