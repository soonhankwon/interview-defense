package dev.soon.interviewdefense.user.controller;

import dev.soon.interviewdefense.security.SecurityUser;
import dev.soon.interviewdefense.user.controller.dto.*;
import dev.soon.interviewdefense.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "유저 관련 API")
public class UserController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "유저 정보 조회 API")
    @GetMapping
    public LoginUserResponse getLoginUser(@Parameter(hidden = true) @AuthenticationPrincipal SecurityUser user) {
        String email = user.getUsername();
        return userService.getUserByEmail(email);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "유저 포지션 변경 API")
    @PatchMapping("/positions")
    public ResponseEntity<String> updateUserPosition(@Parameter(hidden = true) @AuthenticationPrincipal SecurityUser user,
                                                     @RequestBody UserPositionUpdateRequest dto) {
        String email = user.getUsername();
        String res = userService.updateUserPosition(email, dto);
        return ResponseEntity.ok().body(res);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "유저 언어 추가 API")
    @PostMapping("/languages")
    public ResponseEntity<String> addUserLanguages(@Parameter(hidden = true) @AuthenticationPrincipal SecurityUser user,
                                                   @RequestBody UserLanguageRequest dto) {
        String email = user.getUsername();
        String res = userService.addUserLanguages(email, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "유저 언어 삭제 API")
    @DeleteMapping("/languages")
    public ResponseEntity<String> deleteUserLanguage(@Parameter(hidden = true) @AuthenticationPrincipal SecurityUser user,
                                                     @RequestBody UserLanguageDeleteRequest dto) {
        String email = user.getUsername();
        String res = userService.deleteUserLanguage(email, dto);
        return ResponseEntity.ok().body(res);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "유저 기술 추가 API")
    @PostMapping("/techs")
    public ResponseEntity<String> addUserTechs(@Parameter(hidden = true) @AuthenticationPrincipal SecurityUser user,
                                               @RequestBody UserTechRequest dto) {
        String email = user.getUsername();
        String res = userService.addUserTechs(email, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "유저 기술 삭제 API")
    @DeleteMapping("/techs")
    public ResponseEntity<String> deleteUserTech(@Parameter(hidden = true) @AuthenticationPrincipal SecurityUser user,
                                                 @RequestBody UserTechDeleteRequest dto) {
        String email = user.getUsername();
        String res = userService.deleteUserTech(email, dto);
        return ResponseEntity.ok().body(res);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "유저 경력 수정 API")
    @PatchMapping("/work-experience-year")
    public ResponseEntity<String> updateUserWorkExperienceYear(@AuthenticationPrincipal SecurityUser user,
                                                               @RequestBody UserWorkExperienceYearUpdateRequest dto) {
        String email = user.getUsername();
        String res = userService.updateUserWorkExperienceYear(email, dto);
        return ResponseEntity.ok().body(res);
    }
}
