package dev.soon.interviewdefense.chat.service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.service.OpenAiService;
import dev.soon.interviewdefense.chat.controller.dto.ChatMessageDto;
import dev.soon.interviewdefense.chat.controller.dto.ChatRoomReqDto;
import dev.soon.interviewdefense.chat.controller.dto.DefenseChatRoomReqDto;
import dev.soon.interviewdefense.chat.domain.Chat;
import dev.soon.interviewdefense.chat.domain.ChatMessage;
import dev.soon.interviewdefense.chat.domain.ChatSender;
import dev.soon.interviewdefense.chat.domain.ChatTopic;
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
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dev.soon.interviewdefense.chat.util.PromptGenerator.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatServiceV2 implements ChatService {

    private static final String PATTERN = "점수: (\\d+)";
    private static final Pattern SCORE_PATTERN = Pattern.compile(PATTERN);
    private static final String DEFAULT_TOPIC_VALUE = "컴퓨터";

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
    @Transactional
    public Long createDefenseChatRoom(SecurityUser securityUser, DefenseChatRoomReqDto dto) {
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
        String topic;
        if (chat.isTopicDefault()) {
            topic = DEFAULT_TOPIC_VALUE;
        } else {
            topic = chat.getTopic().getValue();
        }
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

    @Override
    public String initDefensePrompt(SecurityUser securityUser, DefenseChatRoomReqDto dto) {
        User user = userRepository.findUserByEmail(securityUser.getUsername()).orElseThrow();
        StringBuilder sb = new StringBuilder();
        user.getLanguages().forEach(i -> sb.append(i.getName().getValue()).append(","));
        String userLanguages = sb.toString();
        sb.setLength(0);

        user.getTechs().forEach(i -> sb.append(i.getName().getValue()).append(","));
        sb.append("기술스택 사용자. ");
        String userTechs = sb.toString();
        sb.setLength(0);
        String topic;
        if (dto.topic() == ChatTopic.DEFAULT) {
            topic = "";
        } else {
            topic = dto.topic().getValue();
        }
        sb.append(INIT_DEFENSE_CHARACTER)
                .append(topic)
                .append(userLanguages)
                .append(userTechs)
                .append(user.getPosition().getValue())
                .append(INIT_DEFENSE_ROLE)
                .append(INIT_DEFENSE_ROLE_KOREAN)
                .append(INIT_DEFENSE_LIMITED);
        String rolePrompt = sb.toString();
        sb.setLength(0);

        sb.append(INIT_DEFENSE_USER_HELLO)
                .append(userLanguages)
                .append(userTechs)
                .append(INIT_DEFENSE_USER_QUESTION);
        String promptUser = sb.toString();

        ChatCompletionRequest request = requestOpenAI(
                new com.theokanning.openai.completion.chat.ChatMessage("system", rolePrompt),
                promptUser);
        ChatCompletionResult chatCompletion = openAiService.createChatCompletion(request);
        return chatCompletion.getChoices().get(0).getMessage().getContent();
    }

    @Override
    public String generateDefensePrompt(Chat chat, SecurityUser securityUser, ChatMessageDto dto) {
        User user = userRepository.findUserByEmail(securityUser.getUsername()).orElseThrow();
        StringBuilder sb = new StringBuilder();
        user.getLanguages().forEach(i -> sb.append(i.getName().getValue()).append(","));
        String userLanguages = sb.toString();
        sb.setLength(0);
        user.getTechs().forEach(i -> sb.append(i.getName().getValue()).append(","));
        String userTechs = sb.toString();
        sb.setLength(0);
        sb.append(DEFENSE_CHARACTER)
                .append(user.getPosition().getValue())
                .append(DEFENSE_ROLE)
                .append(DEFENSE_ROLE_SITUATION)
                .append(DEFENSE_ROLE_SITUATION_OPTION)
                .append(user.getPosition().getValue())
                .append("입니다. 사용 기술스택은")
                .append(userLanguages)
                .append(userTechs)
                .append(" 입니다.")
                .append(DEFENSE_ROLE_QUESTION_OPTION)
                .append(DEFENSE_OPTION_QUESTION_FORBIDDEN_A)
                .append(DEFENSE_OPTION_SCORE)
                .append(DEFENSE_OPTION_SCORE_UNNATURAL);
        String rolePrompt = sb.toString();

        Optional<ChatMessage> optionalChatMessageByAIDesc = chatMessageRepository.findTopByChatOrderByCreatedAtDesc(chat);
        String userPrompt;
        userPrompt = optionalChatMessageByAIDesc
                .map(chatMessage -> "당신이 질문한 내용인 [" + chatMessage.getMessage() + "]에 대한 저의 답변은 " + dto.message() + " 기술면접을 계속 진행해주시고, 연관된 질문을 1개만 해주세요.")
                .orElse("");

        ChatCompletionRequest request = requestOpenAI(new com.theokanning.openai.completion.chat.ChatMessage("system", rolePrompt), userPrompt);
        ChatCompletionResult chatCompletion = openAiService.createChatCompletion(request);
        chat.decreaseAIQuestionMaxNumber();
        if (chat.isDefenseEnd()) {
            String feedbackUser = "지금까지의 대화를 전문가의 입장에서 평가해서 보고서를 만들어주세요. 인터뷰가 끝났습니다.";
            ChatCompletionRequest request1 = requestOpenAI(new com.theokanning.openai.completion.chat.ChatMessage("system", rolePrompt), feedbackUser);
            ChatCompletionResult chatCompletion1 = openAiService.createChatCompletion(request1);
            return "면접 디펜스가 끝났습니다. 감사합니다." + "\n" + chatCompletion1.getChoices().get(0).getMessage().getContent();
        }
        String content = chatCompletion.getChoices().get(0).getMessage().getContent();
        Matcher matcher = SCORE_PATTERN.matcher(content);
        if (matcher.find()) {
            String scoreStr = matcher.group(1);
            int score = Integer.parseInt(scoreStr);
            chat.increaseScore(score);
        }
        return content;
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
