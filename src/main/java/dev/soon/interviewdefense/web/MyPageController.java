package dev.soon.interviewdefense.web;

import dev.soon.interviewdefense.security.SecurityUser;
import dev.soon.interviewdefense.user.domain.Language;
import dev.soon.interviewdefense.user.domain.Tech;
import dev.soon.interviewdefense.user.domain.User;
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
    public String myPage(@AuthenticationPrincipal SecurityUser securityUser, Model model) {
        User loginUserInfo = userService.getLoginUserInfo(securityUser);
        List<Language> loginUserLanguages = userService.getLoginUserLanguages(securityUser);
        List<Tech> loginUserTechs = userService.getLoginUserTechs(securityUser);
        model.addAttribute("user", loginUserInfo);
        model.addAttribute("myLanguages", loginUserLanguages);
        model.addAttribute("myTechs", loginUserTechs);
        return "myPage";
    }

    @GetMapping("/myPage/update")
    public String updateMyPage(@AuthenticationPrincipal SecurityUser securityUser, Model model) {
        User loginUserInfo = userService.getLoginUserInfo(securityUser);
        List<Language> loginUserLanguages = userService.getLoginUserLanguages(securityUser);
        List<Tech> loginUserTechs = userService.getLoginUserTechs(securityUser);
        model.addAttribute("user", loginUserInfo);
        model.addAttribute("myLanguages", loginUserLanguages);
        model.addAttribute("myTechs", loginUserTechs);
        return "myPageUpdateForm";
    }

    @PostMapping("/myPage/update")
    public String edit(@AuthenticationPrincipal SecurityUser securityUser,
                       @ModelAttribute("user") MyPageUpdateForm form) {
        userService.updateMyPage(securityUser, form);
        return "redirect:/myPage";
    }

    @PostMapping("/myPage/languages/add")
    public String addMyLanguage(@AuthenticationPrincipal SecurityUser securityUser,
                                @RequestBody MyLanguageReqDto dto) {
        userService.addMyLanguageInMyPage(securityUser, dto);
        return "redirect:/myPage/update";
    }

    @PostMapping("/myPage/languages/{languageId}/delete")
    public String deleteMyLanguage(@PathVariable Long languageId,
                                   @AuthenticationPrincipal SecurityUser securityUser) {
        userService.deleteMyLanguageInMyPage(securityUser, languageId);
        return "redirect:/myPage/update";
    }

    @PostMapping("/myPage/techs/add")
    public String addMyTech(@AuthenticationPrincipal SecurityUser securityUser,
                            @RequestBody MyTechReqDto dto) {
        userService.addMyTechInMyPage(securityUser, dto);
        return "redirect:/myPage/update";
    }

    @PostMapping("/myPage/techs/{techId}/delete")
    public String deleteMyTech(@PathVariable Long techId,
                               @AuthenticationPrincipal SecurityUser securityUser) {
        userService.deleteMyTechInMyPage(securityUser, techId);
        return "redirect:/myPage/update";
    }
}
