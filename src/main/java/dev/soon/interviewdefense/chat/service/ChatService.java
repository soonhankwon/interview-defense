package dev.soon.interviewdefense.chat.service;

import dev.soon.interviewdefense.chat.controller.dto.ChatRoomReqDto;
import dev.soon.interviewdefense.chat.domain.Chat;
import dev.soon.interviewdefense.security.SecurityUser;

public interface ChatService {
    Chat getChatRoom(SecurityUser securityUser, Long chatRoomId);

    Long createChatRoom(SecurityUser securityUser, ChatRoomReqDto dto);

    void saveMessage(Long chatRoomId, SecurityUser securityUser, String res);
}
