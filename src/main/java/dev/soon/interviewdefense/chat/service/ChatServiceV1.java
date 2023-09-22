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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatServiceV1 implements ChatService {

    private static final String PATTERN = "점수: (\\d+)";
    private static final Pattern SCORE_PATTERN = Pattern.compile(PATTERN);

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
        StringBuilder sb = new StringBuilder();
        sb.append("당신은 답변을 명료하고 정확하게 하는 ")
                .append(chat.getTopic().getValue())
                .append("전문가입니다.");
        String promptRole = sb.toString();
        log.info("prompt role={}", promptRole);
        sb.setLength(0);
        sb.append(dto.message())
                .append("답변은 한국어로 해주세요.")
                .append("답변에 대한 신뢰도 점수를 0~100 사이로 해주세요.")
                .append("복잡한 내용이 있다면 초심자가 이해가능하도록 방법을 사용해서 설명해주세요.");
        String promptUser = sb.toString();
        log.info("prompt user={}", promptUser);

        ChatCompletionRequest request = requestOpenAI(new com.theokanning.openai.completion.chat.ChatMessage("system", promptRole), promptUser);
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
        if(dto.topic() == ChatTopic.DEFAULT) {
            topic = "";
        } else {
            topic = dto.topic().getValue();
        }
        sb.append("당신은 질문을 명확하게 하는")
                .append(topic)
                .append(userLanguages)
                .append(userTechs)
                .append("전문가입니다.")
                .append("그리고 IT 기업 한국인 면접관입니다.")
                .append("질문은 1개만 해주세요.");
        String promptRole = sb.toString();

        sb.setLength(0);
        sb.append("안녕하세요 ")
                .append(userLanguages)
                .append(userTechs)
                .append("인 저에게 기술 면접 질문을 해주세요");
        String promptUser = sb.toString();

        ChatCompletionRequest request = requestOpenAI(new com.theokanning.openai.completion.chat.ChatMessage("system", promptRole), promptUser);
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
        sb.append("당신은 질문을 명료하게 하며 꼬리질문을 잘하는")
                .append(" IT 기업 한국인 면접관입니다.")
                .append("현재 당신은 면접관으로 들어와있습니다.")
                .append("답변은 모두 면접자와 하는 대화입니다. 부자연스러운 답변은 하지 말아주세요.")
                .append("당신이 면접할 대상의 포지션은 ")
                .append(user.getPosition().getValue())
                .append(" 입니다. 사용 프로그래밍 언어는 ")
                .append(userLanguages)
                .append(" 사용 기술스택은")
                .append(userTechs)
                .append(" 입니다.")
                .append("면접 상황에 맞지 않는 답변을 받는다면 다음 질문을 해주세요")
                .append("답변에 대해 점수를 0~100 까지로 평가해서 점수: 로 맨앞에 표시해주세요.")
                .append("단답형 답변에 대해서는 꼭 추가 질문을 해주세요. 이경우 점수는50 입니다.")
                .append("모르겠다는 질문의 점수도50 입니다.")
                .append("점수가 80 이상이라면 다음 질문을 꼭 해주세요.")
                .append(user.getPosition().getValue())
                .append(" 에 대한 다양한 질문을 해주세요")
                .append("질문은 꼭 하나만 해주세요.")
                .append("한번에 여러 부분에 대한 질문은 하지말아주세요. ")
                .append("예를 들어 당신은 자바와 스프링, 스프링 부트, JPA 에 대해 얼마나 자세히 알고 있나요?")
                .append("와 같은 질문은 너무 많은 주제를 담고 있습니다.")
                .append("또한 같은 질문은 하지마세요.");
        String promptRole = sb.toString();

        sb.setLength(0);
        sb.append("당신의 질문에 대한 답변은")
                .append(dto.message())
                .append("입니다.")
                .append("저의 답변이 좋았다면 다음 질문으로 꼭 넘어가 주세요.");
        String promptUser = sb.toString();

        ChatCompletionRequest request = requestOpenAI(new com.theokanning.openai.completion.chat.ChatMessage("system", promptRole), promptUser);
        ChatCompletionResult chatCompletion = openAiService.createChatCompletion(request);
        chat.decreaseAIQuestionMaxNumber();
        if(chat.isDefenseEnd()) {
            String feedbackUser = "지금까지의 대화를 전문가의 입장에서 상세하게 평가해서 보고서를 만들어주세요. 인터뷰가 끝났습니다.";
            ChatCompletionRequest request1 = requestOpenAI(new com.theokanning.openai.completion.chat.ChatMessage("system", promptRole), feedbackUser);
            ChatCompletionResult chatCompletion1 = openAiService.createChatCompletion(request1);
            return "면접 디펜스가 끝났습니다. 감사합니다." + "\n" + chatCompletion1.getChoices().get(0).getMessage().getContent();
        }
        String content = chatCompletion.getChoices().get(0).getMessage().getContent();
        Matcher matcher = SCORE_PATTERN.matcher(content);
        if(matcher.find()) {
            String scoreStr = matcher.group(1);
            int score = Integer.parseInt(scoreStr);
            chat.increaseScore(score);
        }
        return content;
    }

    private ChatCompletionRequest requestOpenAI(com.theokanning.openai.completion.chat.ChatMessage promptRole, String promptUser) {
        return ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(List.of(
                        promptRole,
                        new com.theokanning.openai.completion.chat.ChatMessage("user", promptUser)
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
