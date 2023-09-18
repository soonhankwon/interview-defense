package dev.soon.interviewdefense.chat.service;

import dev.soon.interviewdefense.chat.controller.dto.ChatRoomReqDto;
import dev.soon.interviewdefense.chat.domain.Chat;
import dev.soon.interviewdefense.chat.domain.ChatMessage;
import dev.soon.interviewdefense.chat.respository.ChatMessageRepository;
import dev.soon.interviewdefense.chat.respository.ChatRepository;
import dev.soon.interviewdefense.security.SecurityUser;
import dev.soon.interviewdefense.user.domain.User;
import dev.soon.interviewdefense.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatServiceV1 implements ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    @Transactional
    public Long createChatRoom(SecurityUser securityUser, ChatRoomReqDto dto) {
        User user = userRepository.findUserByEmail(securityUser.getUsername())
                .orElseThrow();
        Chat chat = new Chat(dto, user);
        chatRepository.save(chat);
        return chat.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Chat getChatRoom(SecurityUser securityUser, Long chatRoomId) {
        return chatRepository.findById(chatRoomId)
                .orElseThrow();
    }

    @Override
    @Transactional
    public void saveMessage(Long chatRoomId, SecurityUser securityUser, String res) {
        Chat chatRoom = chatRepository.findById(chatRoomId).orElseThrow();
        ChatMessage message = new ChatMessage(res, chatRoom);
        chatMessageRepository.save(message);
    }
}
