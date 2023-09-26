package dev.soon.interviewdefense.chat.service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.service.OpenAiService;
import dev.soon.interviewdefense.chat.controller.dto.ChatMessageDto;
import dev.soon.interviewdefense.chat.controller.dto.ChatRoomReqDto;
import dev.soon.interviewdefense.chat.domain.Chat;
import dev.soon.interviewdefense.chat.domain.ChatMessage;
import dev.soon.interviewdefense.chat.domain.ChatSender;
import dev.soon.interviewdefense.chat.respository.ChatMessageRepository;
import dev.soon.interviewdefense.chat.respository.ChatRepository;
import dev.soon.interviewdefense.security.SecurityUser;
import dev.soon.interviewdefense.user.domain.User;
import dev.soon.interviewdefense.user.repository.UserRepository;
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
    @Transactional(readOnly = true)
    public List<ChatMessage> getChatRoomMessages(Chat chatRoom) {
        return chatMessageRepository.findChatMessagesByChat(chatRoom);
    }

    @Override
    @Transactional
    public Chat saveUserMessage(Long chatRoomId, SecurityUser securityUser, ChatMessageDto dto) {
        User user = userRepository.findUserByEmail(securityUser.getUsername())
                .orElseThrow();
        Chat chat = chatRepository.findChatByIdAndUser(chatRoomId, user)
                .orElseThrow();
        ChatMessage chatMessage = new ChatMessage(dto.message(), chat, ChatSender.USER);
        chat.saveMessage(chatMessage);
        return chat;
    }

    @Override
    @Transactional
    public void saveAIMessage(Long chatRoomId, SecurityUser securityUser, String aiMessage) {
        User user = userRepository.findUserByEmail(securityUser.getUsername())
                .orElseThrow();
        Chat chat = chatRepository.findChatByIdAndUser(chatRoomId, user)
                .orElseThrow();
        ChatMessage chatMessage = new ChatMessage(aiMessage, chat, ChatSender.AI);
        chat.saveMessage(chatMessage);
    }

    @Override
    public String generatePrompt(Chat chat, ChatMessageDto dto) {
        String topic = chat.getTopic().getValue();
        String rolePrompt = MENTOR_ROLE_CHARACTER +
                topic +
                MENTOR_ROLE +
                MENTOR_OPTION_TEACHING_STYLE +
                MENTOR_OPTION_KOREAN +
                MENTOR_OPTION_SCORE;

        ChatCompletionRequest request = requestOpenAI(
                new com.theokanning.openai.completion.chat.ChatMessage("system", rolePrompt),
                dto.message());
        ChatCompletionResult chatCompletion = openAiService.createChatCompletion(request);
        return chatCompletion.getChoices().get(0).getMessage().getContent();
    }

    private ChatCompletionRequest requestOpenAI(com.theokanning.openai.completion.chat.ChatMessage rolePrompt, String userPrompt) {
        return ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(List.of(
                        rolePrompt,
                        new com.theokanning.openai.completion.chat.ChatMessage("user", userPrompt)
                ))
                .maxTokens(1000)
                .build();
    }

    @Override
    @Transactional
    public void deleteChat(Long chatRoomId, SecurityUser securityUser) {
        User user = userRepository.findUserByEmail(securityUser.getUsername()).orElseThrow();
        Chat chat = chatRepository.findChatByUserAndId(user, chatRoomId).orElseThrow();
        chatRepository.delete(chat);
    }
}
