package dev.soon.interviewdefense.chat.service;

import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import dev.soon.interviewdefense.chat.controller.dto.ChatRoomReqDto;
import dev.soon.interviewdefense.chat.domain.Chat;
import dev.soon.interviewdefense.chat.domain.ChatMessage;
import dev.soon.interviewdefense.chat.respository.ChatCacheStore;
import dev.soon.interviewdefense.chat.respository.ChatMessageRepository;
import dev.soon.interviewdefense.chat.respository.ChatRepository;
import dev.soon.interviewdefense.exception.ApiException;
import dev.soon.interviewdefense.exception.CustomErrorCode;
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
    private final ChatCacheStore chatCacheStore;

    @Override
    @Transactional
    public Long createChatRoom(String email, ChatRoomReqDto dto) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_USER_IN_DB));

        Chat chat = new Chat(dto, user);
        chatRepository.save(chat);

        Long chatId = chat.getId();
        chatCacheStore.cacheChat(chat);
        return chatId;
    }

    @Override
    @Transactional(readOnly = true)
    public Chat getChatRoom(String email, Long chatId) {
        if(chatCacheStore.existsCacheByEmail(chatId)) {
            return chatCacheStore.getChatByCacheKey(chatId);
        }
        else {
            Chat chat = chatRepository.findById(chatId)
                    .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_CHATROOM_IN_DB));
            chatCacheStore.cacheChat(chat);
            return chat;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessage> getChatRoomMessages(Chat chatRoom) {
        return chatMessageRepository.findChatMessagesByChat(chatRoom);
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
                .maxTokens(100)
                .stream(true)
                .build();
    }

    @Override
    @Transactional
    public void deleteChat(Long chatId, String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_USER_IN_DB));

        Chat chat = chatRepository.findChatByIdAndUser(chatId, user)
                .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_MATCHES_CHAT_ID_AND_USER));

        chatRepository.delete(chat);
        chatCacheStore.removeCache(chatId);
    }
}
