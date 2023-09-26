package dev.soon.interviewdefense.chat.service;

import dev.soon.interviewdefense.chat.controller.dto.ChatMessageDto;
import dev.soon.interviewdefense.chat.controller.dto.ChatRoomReqDto;
import dev.soon.interviewdefense.chat.domain.Chat;
import dev.soon.interviewdefense.chat.domain.ChatMessage;
import dev.soon.interviewdefense.security.SecurityUser;

import java.util.List;

public interface ChatService {
    Chat getChatRoom(SecurityUser securityUser, Long chatRoomId);

    Long createChatRoom(SecurityUser securityUser, ChatRoomReqDto dto);

    Chat saveUserMessage(Long chatRoomId, SecurityUser securityUser, ChatMessageDto dto);

    void saveAIMessage(Long chatRoomId, SecurityUser securityUser, String message);

    String generatePrompt(Chat chat, ChatMessageDto dto);

    void deleteChat(Long chatRoomId, SecurityUser securityUser);

    List<ChatMessage> getChatRoomMessages(Chat chatRoom);
}
