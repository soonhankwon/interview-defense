package dev.soon.interviewdefense.chat.controller;

import dev.soon.interviewdefense.chat.controller.dto.ChatMessageDto;
import dev.soon.interviewdefense.chat.controller.dto.ChatRoomReqDto;
import dev.soon.interviewdefense.chat.domain.Chat;
import dev.soon.interviewdefense.chat.domain.ChatMessage;
import dev.soon.interviewdefense.chat.respository.ChatMessageRepository;
import dev.soon.interviewdefense.chat.service.ChatService;
import dev.soon.interviewdefense.security.SecurityUser;
import dev.soon.interviewdefense.user.domain.User;
import dev.soon.interviewdefense.user.service.UserService;
import io.github.flashvayne.chatgpt.service.ChatgptService;
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

    private final ChatgptService chatgptService;
    private final ChatService chatService;
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
        List<ChatMessage> chatMessagesByChat = chatMessageRepository.findChatMessagesByChat(chatRoom);

        model.addAttribute("chatMessages", chatMessagesByChat);
        model.addAttribute("chat", chatRoom);
        User loginUserInfo = userServiceImpl.getLoginUserInfo(securityUser);
        model.addAttribute("user", loginUserInfo);
        model.addAttribute("chatMessageDto", new ChatMessageDto(null));
        return "chatRoom";
    }

    @PostMapping("/{chatRoomId}")
    public String submitMessage(@PathVariable Long chatRoomId,
                                @AuthenticationPrincipal SecurityUser securityUser,
                                @ModelAttribute("chatMessageDto") ChatMessageDto dto) {
        log.info("dto={}", dto);
        String res = chatgptService.sendMessage(dto.message());
        chatService.saveMessage(chatRoomId, securityUser, res);
        log.info("res={}", res);
        return "redirect:/chat/{chatRoomId}";
    }
}
