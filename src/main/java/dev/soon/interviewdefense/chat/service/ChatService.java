package dev.soon.interviewdefense.chat.service;

import dev.soon.interviewdefense.chat.controller.dto.ChatMessageRecordResponse;
import dev.soon.interviewdefense.chat.controller.dto.ChatRequest;
import dev.soon.interviewdefense.chat.controller.dto.ChatResponse;
import dev.soon.interviewdefense.chat.domain.Chat;
import dev.soon.interviewdefense.chat.domain.ChatMessage;
import dev.soon.interviewdefense.chat.respository.ChatMessageRepository;
import dev.soon.interviewdefense.chat.respository.ChatRepository;
import dev.soon.interviewdefense.exception.ApiException;
import dev.soon.interviewdefense.exception.CustomErrorCode;
import dev.soon.interviewdefense.user.domain.User;
import dev.soon.interviewdefense.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;

    public List<ChatResponse> getChatsByUser(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_USER_IN_DB));

        return chatRepository.findChatsByUser(user)
                .stream()
                .map(Chat::ofResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long createChat(String email, ChatRequest dto) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_USER_IN_DB));

        Chat chat = new Chat(dto, user);
        chatRepository.save(chat);
        return chat.getId();
    }

    public List<ChatMessageRecordResponse> getChatMessageRecordByChat(Long chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_CHATROOM_IN_DB));

        List<ChatMessage> chatMessagesByChat = chatMessageRepository.findChatMessagesByChat(chat);

        return chatMessagesByChat.stream()
                .map(ChatMessage::ofResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteChat(String email, Long chatId) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_USER_IN_DB));

        Chat chat = chatRepository.findChatByIdAndUser(chatId, user)
                .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_CHATROOM_IN_DB));

        chatRepository.delete(chat);
    }
}
