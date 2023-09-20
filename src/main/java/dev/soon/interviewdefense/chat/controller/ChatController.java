package dev.soon.interviewdefense.chat.controller;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.service.OpenAiService;
import dev.soon.interviewdefense.chat.controller.dto.ChatMessageDto;
import dev.soon.interviewdefense.chat.controller.dto.ChatRoomReqDto;
import dev.soon.interviewdefense.chat.domain.Chat;
import dev.soon.interviewdefense.chat.domain.ChatMessage;
import dev.soon.interviewdefense.chat.domain.ChatSender;
import dev.soon.interviewdefense.chat.respository.ChatMessageRepository;
import dev.soon.interviewdefense.chat.service.ChatService;
import dev.soon.interviewdefense.security.SecurityUser;
import dev.soon.interviewdefense.user.domain.User;
import dev.soon.interviewdefense.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;
    private final OpenAiService openAiService;
    private final UserService userServiceImpl;
    private final ChatMessageRepository chatMessageRepository;

    @GetMapping("/create")
    public String chatRoom(Model model) {
        model.addAttribute("chat", new Chat());
        return "chatRoomForm";
    }


    @PostMapping("/create")
    public String createChatRoom(@AuthenticationPrincipal SecurityUser securityUser,
                                 @ModelAttribute("chat") ChatRoomReqDto dto) {
        Long chatRoomId = chatService.createChatRoom(securityUser, dto);
        return "redirect:/chat/" + chatRoomId;
    }

    @GetMapping("/{chatRoomId}")
    public String getChatRoom(@AuthenticationPrincipal SecurityUser securityUser,
                              @PathVariable Long chatRoomId, Model model) {
        Chat chatRoom = chatService.getChatRoom(securityUser, chatRoomId);
        List<ChatMessage> chatMessagesInChatRoom = chatMessageRepository.findChatMessagesByChat(chatRoom);

        model.addAttribute("chatMessages", chatMessagesInChatRoom);
        model.addAttribute("chat", chatRoom);
        User loginUserInfo = userServiceImpl.getLoginUserInfo(securityUser);
        model.addAttribute("user", loginUserInfo);
        model.addAttribute("chatMessageDto", new ChatMessageDto(null));
        return "chatRoom";
    }

    @PostMapping("/{chatRoomId}")
    public String sendMessage(@PathVariable Long chatRoomId,
                              @AuthenticationPrincipal SecurityUser securityUser,
                              @ModelAttribute("chatMessageDto") ChatMessageDto dto) {
        log.info("dto={}", dto);
        chatService.saveMessage(chatRoomId, securityUser, dto.message(), ChatSender.USER);
        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(List.of(
                        new com.theokanning.openai.completion.chat.ChatMessage("system", "당신은 자바 전문가입니다."),
                        new com.theokanning.openai.completion.chat.ChatMessage("user", dto.message())
                ))
                .maxTokens(1000)
                .build();
        ChatCompletionResult chatCompletion = openAiService.createChatCompletion(request);
        String response = chatCompletion.getChoices().get(0).getMessage().getContent();
        log.info("response={}", response);
        chatService.saveMessage(chatRoomId, securityUser, response, ChatSender.AI);
        return "redirect:/chat/{chatRoomId}";
    }

    @PostMapping("/{chatRoomId}/delete")
    public String deleteChat(@PathVariable Long chatRoomId,
                             @AuthenticationPrincipal SecurityUser securityUser) {
        chatService.deleteChat(chatRoomId, securityUser);
        return "redirect:/";
    }
}
