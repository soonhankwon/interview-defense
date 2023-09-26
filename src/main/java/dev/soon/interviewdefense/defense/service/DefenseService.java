package dev.soon.interviewdefense.defense.service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.service.OpenAiService;
import dev.soon.interviewdefense.chat.controller.dto.ChatMessageDto;
import dev.soon.interviewdefense.chat.controller.dto.DefenseChatRoomReqDto;
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

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dev.soon.interviewdefense.chat.util.PromptGenerator.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class DefenseService {

    private static final String PATTERN = "점수: (\\d+)";
    private static final Pattern SCORE_PATTERN = Pattern.compile(PATTERN);

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    private final OpenAiService openAiService;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public Long createDefenseChatRoom(SecurityUser securityUser, DefenseChatRoomReqDto dto) {
        User user = userRepository.findUserByEmail(securityUser.getUsername())
                .orElseThrow();
        Chat chat = new Chat(dto, user);
        chatRepository.save(chat);
        return chat.getId();
    }


    public String initDefensePrompt(SecurityUser securityUser, DefenseChatRoomReqDto dto) {
        User user = userRepository.findUserByEmail(securityUser.getUsername()).orElseThrow();
        StringBuilder sb = new StringBuilder();
        user.getTechs().forEach(i -> sb.append(i.getName().getValue()).append(","));
        String userTechs = sb.toString();
        sb.setLength(0);

        String topic = dto.topic().getValue();
        sb.append(INIT_DEFENSE_CHARACTER)
                .append(topic)
                .append(INIT_DEFENSE_ROLE)
                .append(INIT_DEFENSE_ROLE_KOREAN)
                .append(topic)
                .append("주제의 질문을 1개 해주세요.");

        String rolePrompt = sb.toString();
        sb.setLength(0);

        sb.append(INIT_DEFENSE_USER_HELLO)
                .append(userTechs)
                .append(INIT_DEFENSE_USER_QUESTION);
        String promptUser = sb.toString();

        ChatCompletionRequest request = requestOpenAI(
                new com.theokanning.openai.completion.chat.ChatMessage("system", rolePrompt),
                promptUser);
        ChatCompletionResult chatCompletion = openAiService.createChatCompletion(request);
        return chatCompletion.getChoices().get(0).getMessage().getContent();
    }

    public String generateDefensePrompt(Chat chat, SecurityUser securityUser, ChatMessageDto dto) {
        User user = userRepository.findUserByEmail(securityUser.getUsername()).orElseThrow();
        StringBuilder sb = new StringBuilder();
        sb.append(DEFENSE_CHARACTER)
                .append(chat.getTopic().getValue()).append(",")
                .append(user.getPosition().getValue())
                .append(DEFENSE_ROLE)
                .append(DEFENSE_ROLE_SITUATION)
                .append("상대방의 답변에 대한 점수를 꼭 점수:0~100 으로 평가해주세요.")
                .append("연관된 추가적인 질문을 1개 해주세요. 다만")
                .append(DEFENSE_OPTION_SCORE_GREAT)
                .append(chat.getTopic().getValue())
                .append("에 대한 다른 질문을 해주세요.")
                .append(DEFENSE_OPTION_SCORE_DONT_KNOW);
        String rolePrompt = sb.toString();

        Optional<ChatMessage> optionalChatMessageByAIDesc = chatMessageRepository.findTopByChatOrderByCreatedAtDesc(chat);
        String userPrompt = optionalChatMessageByAIDesc
                .map(chatMessage -> "당신이 질문한 내용인 [" + chatMessage.getMessage() + "]에 대한 저의 답변은 [" + dto.message() + "] 입니다.")
                .orElse("");

        ChatCompletionRequest request = requestOpenAI(new com.theokanning.openai.completion.chat.ChatMessage("system", rolePrompt), userPrompt);
        ChatCompletionResult chatCompletion = openAiService.createChatCompletion(request);
        chat.decreaseAIQuestionMaxNumber();
        if (chat.isDefenseEnd()) {
            String feedbackUser = "지금까지의 대화를 전문가의 입장에서 평가해서 보고서를 만들어주세요. 추가적인 질문은 하지마세요.";
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
}
