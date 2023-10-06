package dev.soon.interviewdefense.chat.service;

import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import dev.soon.interviewdefense.chat.controller.dto.ChatMessageDto;
import dev.soon.interviewdefense.chat.controller.dto.ChatRoomReqDto;
import dev.soon.interviewdefense.chat.domain.Chat;
import dev.soon.interviewdefense.chat.domain.ChatMessage;
import dev.soon.interviewdefense.chat.domain.ChatSender;
import dev.soon.interviewdefense.chat.respository.ChatMessageRepository;
import dev.soon.interviewdefense.chat.respository.ChatRepository;
import dev.soon.interviewdefense.exception.ApiException;
import dev.soon.interviewdefense.exception.CustomErrorCode;
import dev.soon.interviewdefense.security.SecurityUser;
import dev.soon.interviewdefense.user.domain.User;
import dev.soon.interviewdefense.user.repository.UserRepository;
import io.reactivex.Flowable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static dev.soon.interviewdefense.chat.util.PromptGenerator.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatServiceV2 implements ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    private final OpenAiService openAiService;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    @Transactional
    public Long createChatRoom(SecurityUser securityUser, ChatRoomReqDto dto) {
        User user = userRepository.findUserByEmail(securityUser.getUsername())
                .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_USER_IN_DB));
        Chat chat = new Chat(dto, user);
        chatRepository.save(chat);
        return chat.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Chat getChatRoom(SecurityUser securityUser, Long chatRoomId) {
        return chatRepository.findById(chatRoomId)
                .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_CHATROOM_IN_DB));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessage> getChatRoomMessages(Chat chatRoom) {
        return chatMessageRepository.findChatMessagesByChat(chatRoom);
    }

    @Override
    @Transactional
    public Chat saveUserMessage(Long chatRoomId, SecurityUser securityUser, ChatMessageDto dto) {
        User user = userRepository.findUserByEmail(securityUser.getUsername())
                .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_USER_IN_DB));
        Chat chat = chatRepository.findChatByIdAndUser(chatRoomId, user)
                .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_MATCHES_CHAT_ID_AND_USER));
        ChatMessage chatMessage = new ChatMessage(dto.message(), chat, ChatSender.USER);
        chat.saveMessage(chatMessage);
        return chat;
    }

    @Override
    @Transactional
    public void saveAIMessage(Long chatRoomId, SecurityUser securityUser, String aiMessage) {
        User user = userRepository.findUserByEmail(securityUser.getUsername())
                .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_USER_IN_DB));
        Chat chat = chatRepository.findChatByIdAndUser(chatRoomId, user)
                .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_MATCHES_CHAT_ID_AND_USER));
        ChatMessage chatMessage = new ChatMessage(aiMessage, chat, ChatSender.AI);
        chat.saveMessage(chatMessage);
    }

    @Override
    public Flowable<ChatCompletionChunk> generateStreamResponse(Chat chat, String question) {
        String topic = chat.getTopic().getValue();
        String rolePrompt = MENTOR_ROLE_CHARACTER +
                topic +
                MENTOR_ROLE +
                MENTOR_OPTION_TEACHING_STYLE +
                MENTOR_OPTION_KOREAN;

        ChatCompletionRequest request = requestOpenAI(rolePrompt, question);
        return openAiService.streamChatCompletion(request);
    }

    private ChatCompletionRequest requestOpenAI(String rolePrompt, String userPrompt) {
        return ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(List.of(new com.theokanning.openai.completion.chat.ChatMessage("system", rolePrompt),
                        new com.theokanning.openai.completion.chat.ChatMessage("user", userPrompt)))
                .maxTokens(1000)
                .stream(true)
                .build();
    }

    @Override
    @Transactional
    public void deleteChat(Long chatRoomId, SecurityUser securityUser) {
        User user = userRepository.findUserByEmail(securityUser.getUsername())
                .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_USER_IN_DB));
        Chat chat = chatRepository.findChatByUserAndId(user, chatRoomId)
                .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_MATCHES_CHAT_ID_AND_USER));
        chatRepository.delete(chat);
    }
}
