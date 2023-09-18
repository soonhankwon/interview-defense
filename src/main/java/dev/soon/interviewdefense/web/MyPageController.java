package dev.soon.interviewdefense.web;

import dev.soon.interviewdefense.security.SecurityUser;
import dev.soon.interviewdefense.user.domain.User;
import dev.soon.interviewdefense.user.service.UserService;
import dev.soon.interviewdefense.web.dto.MyPageUpdateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MyPageController {
    private final UserService userService;

    @GetMapping("/myPage")
    public String myPage(@AuthenticationPrincipal SecurityUser securityUser, Model model) {
        User loginUserInfo = userService.getLoginUserInfo(securityUser);
        model.addAttribute("user", loginUserInfo);
        return "myPage";
    }

    @GetMapping("/myPage/update")
    public String updateMyPage(@AuthenticationPrincipal SecurityUser securityUser, Model model) {
        User loginUserInfo = userService.getLoginUserInfo(securityUser);
        model.addAttribute("user", loginUserInfo);
        return "myPageUpdateForm";
    }

    @PostMapping("/myPage/update")
    public String edit(@AuthenticationPrincipal SecurityUser securityUser,
                       @ModelAttribute("user") MyPageUpdateForm form) {
        userService.updateMyPage(securityUser, form);
        return "redirect:/myPage";
    }
}
