package dev.soon.interviewdefense.web;

import dev.soon.interviewdefense.security.SecurityUser;
import dev.soon.interviewdefense.user.domain.User;
import dev.soon.interviewdefense.user.domain.UserLanguage;
import dev.soon.interviewdefense.user.domain.UserTech;
import dev.soon.interviewdefense.user.service.UserService;
import dev.soon.interviewdefense.web.dto.MyLanguageReqDto;
import dev.soon.interviewdefense.web.dto.MyPageUpdateForm;
import dev.soon.interviewdefense.web.dto.MyTechReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MyPageController {
    private final UserService userService;

    @GetMapping("/myPage")
    public String myPage(@AuthenticationPrincipal SecurityUser securityUser,
                         Model model) {
        String email = getEmailBySecurityUser(securityUser);
        User loginUser = userService.getUserByEmail(email);
        List<UserLanguage> loginUserLanguages = userService.getUserLanguages(email);
        List<UserTech> loginUserTechs = userService.getUserTechs(email);

        model.addAttribute("user", loginUser);
        model.addAttribute("myLanguages", loginUserLanguages);
        model.addAttribute("myTechs", loginUserTechs);
        return "myPage";
    }

    private String getEmailBySecurityUser(SecurityUser securityUser) {
        return securityUser.getUsername();
    }

    @GetMapping("/myPage/update")
    public String updateMyPage(@AuthenticationPrincipal SecurityUser securityUser,
                               Model model) {
        String email = getEmailBySecurityUser(securityUser);
        User loginUser = userService.getUserByEmail(email);
        List<UserLanguage> loginUserLanguages = userService.getUserLanguages(email);
        List<UserTech> loginUserTechs = userService.getUserTechs(email);

        model.addAttribute("user", loginUser);
        model.addAttribute("myLanguages", loginUserLanguages);
        model.addAttribute("myTechs", loginUserTechs);
        return "myPageUpdateForm";
    }

    @PostMapping("/myPage/update")
    public String edit(@AuthenticationPrincipal SecurityUser securityUser,
                       @ModelAttribute("user") MyPageUpdateForm form) {
        String email = getEmailBySecurityUser(securityUser);
        userService.updateMyPage(email, form);
        return "redirect:/myPage";
    }

    @PostMapping("/myPage/languages/add")
    public String addMyLanguage(@AuthenticationPrincipal SecurityUser securityUser,
                                @RequestBody MyLanguageReqDto dto) {
        String email = getEmailBySecurityUser(securityUser);
        userService.addMyLanguageInMyPage(email, dto);
        return "redirect:/myPage/update";
    }

    @PostMapping("/myPage/languages/{languageId}/delete")
    public String deleteMyLanguage(@PathVariable Long languageId,
                                   @AuthenticationPrincipal SecurityUser securityUser) {
        String email = getEmailBySecurityUser(securityUser);
        userService.deleteMyLanguageInMyPage(email, languageId);
        return "redirect:/myPage/update";
    }

    @PostMapping("/myPage/techs/add")
    public String addMyTech(@AuthenticationPrincipal SecurityUser securityUser,
                            @RequestBody MyTechReqDto dto) {
        String email = getEmailBySecurityUser(securityUser);
        userService.addMyTechInMyPage(email, dto);
        return "redirect:/myPage/update";
    }

    @PostMapping("/myPage/techs/{techId}/delete")
    public String deleteMyTech(@PathVariable Long techId,
                               @AuthenticationPrincipal SecurityUser securityUser) {
        String email = getEmailBySecurityUser(securityUser);
        userService.deleteMyTechInMyPage(email, techId);
        return "redirect:/myPage/update";
    }
}
