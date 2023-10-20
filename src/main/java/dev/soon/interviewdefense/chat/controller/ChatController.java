package dev.soon.interviewdefense.chat.controller;

import dev.soon.interviewdefense.chat.controller.dto.ChatRoomReqDto;
import dev.soon.interviewdefense.chat.domain.Chat;
import dev.soon.interviewdefense.chat.domain.ChatMessage;
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
    private final ChatService chatServiceV2;
    private final UserService userService;

    @GetMapping("/create")
    public String chatRoom(Model model) {
        model.addAttribute("chat", new Chat());
        return "chatRoomForm";
    }

    @PostMapping("/create")
    public String createChatRoom(@AuthenticationPrincipal SecurityUser securityUser,
                                 @ModelAttribute("chat") ChatRoomReqDto dto) {
        String email = securityUser.getUsername();
        Long chatRoomId = chatServiceV2.createChatRoom(email, dto);
        return "redirect:/chat/" + chatRoomId;
    }

    @GetMapping("/{chatRoomId}")
    public String getChatRoom(@AuthenticationPrincipal SecurityUser securityUser,
                              @PathVariable Long chatRoomId,
                              Model model) {
        String email = securityUser.getUsername();
        Chat chatRoom = chatServiceV2.getChatRoom(email, chatRoomId);
        List<ChatMessage> chatMessagesInChatRoom = chatServiceV2.getChatRoomMessages(chatRoom);
        model.addAttribute("chatMessages", chatMessagesInChatRoom);

        User loginUser = userService.getUserByEmail(email);
        model.addAttribute("user", loginUser);
        return "chatRoom";
    }

    @PostMapping("/{chatRoomId}/delete")
    public String deleteChat(@PathVariable Long chatRoomId,
                             @AuthenticationPrincipal SecurityUser securityUser) {
        String email = securityUser.getUsername();
        chatServiceV2.deleteChat(chatRoomId, email);
        return "redirect:/";
    }
}
