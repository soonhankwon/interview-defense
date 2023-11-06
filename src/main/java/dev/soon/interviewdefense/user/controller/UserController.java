package dev.soon.interviewdefense.user.controller;

import dev.soon.interviewdefense.security.SecurityUser;
import dev.soon.interviewdefense.user.controller.dto.*;
import dev.soon.interviewdefense.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public LoginUserResponse getLoginUser(@AuthenticationPrincipal SecurityUser user) {
        String email = user.getUsername();
        return userService.getUserByEmail(email);
    }

    @PatchMapping("/positions")
    public ResponseEntity<String> updateUserPosition(@AuthenticationPrincipal SecurityUser user,
                                             @RequestBody UserPositionUpdateRequest dto) {
        String email = user.getUsername();
        String res = userService.updateUserPosition(email, dto);
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/languages")
    public ResponseEntity<String> addUserLanguages(@AuthenticationPrincipal SecurityUser user,
                                 @RequestBody UserLanguageRequest dto) {
        String email = user.getUsername();
        String res = userService.addUserLanguages(email, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @DeleteMapping("/languages")
    public ResponseEntity<String> deleteUserLanguage(@AuthenticationPrincipal SecurityUser user,
                                   @RequestBody UserLanguageDeleteRequest dto) {
        String email = user.getUsername();
        String res = userService.deleteUserLanguage(email, dto);
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/techs")
    @Transactional
    public ResponseEntity<String> addUserTechs(@AuthenticationPrincipal SecurityUser user,
                             @RequestBody UserTechRequest dto) {
        String email = user.getUsername();
        String res = userService.addUserTechs(email, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @DeleteMapping("/techs")
    public ResponseEntity<String> deleteUserTech(@AuthenticationPrincipal SecurityUser user,
                               @RequestBody UserTechDeleteRequest dto) {
        String email = user.getUsername();
        String res = userService.deleteUserTech(email, dto);
        return ResponseEntity.ok().body(res);
    }

    @PatchMapping("/work-experience-year")
    public ResponseEntity<String> updateUserWorkExperienceYear(@AuthenticationPrincipal SecurityUser user,
                               @RequestBody UserWorkExperienceYearUpdateRequest dto) {
        String email = user.getUsername();
        String res = userService.updateUserWorkExperienceYear(email, dto);
        return ResponseEntity.ok().body(res);
    }
}
