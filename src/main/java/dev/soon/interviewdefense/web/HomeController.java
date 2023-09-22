package dev.soon.interviewdefense.web;

import dev.soon.interviewdefense.chat.domain.Chat;
import dev.soon.interviewdefense.chat.respository.ChatRepository;
import dev.soon.interviewdefense.security.TokenStatus;
import dev.soon.interviewdefense.security.service.JwtService;
import dev.soon.interviewdefense.user.domain.User;
import dev.soon.interviewdefense.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;

    @GetMapping("/")
    public String home(@CookieValue(name = "AccessToken", required = false) String token, Model model) {
        if(token == null) {
            return "home";
        }
        if(jwtService.validateToken(token) != TokenStatus.VALID) {
            return "home";
        }

        String email = jwtService.getSubjectFromToken(token);
        User user = userRepository.findUserByEmail(email).get();
        List<Chat> chats = chatRepository.findChatsByUser(user);
        List<Chat> defenseRankChats = chatRepository.findClosedDefenseChatsOrderByScoreDesc();
        model.addAttribute("chats", chats);
        model.addAttribute("user", user);
        model.addAttribute("rank", defenseRankChats);
        return "loginHome";
    }
}
