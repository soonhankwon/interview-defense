package dev.soon.interviewdefense.oauth.controller;

import dev.soon.interviewdefense.oauth.controller.dto.KakaoOauthLoginParam;
import dev.soon.interviewdefense.oauth.controller.dto.NaverOauthLoginParam;
import dev.soon.interviewdefense.oauth.service.OauthService;
import dev.soon.interviewdefense.security.service.JwtService;
import dev.soon.interviewdefense.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Oauth2 관련 API")
public class OauthController {

    private final OauthService<KakaoOauthLoginParam> kakaoOauthService;
    private final OauthService<NaverOauthLoginParam> naverOauthService;
    private final JwtService jwtService;

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "oauth & Header JWT 발급 API")
    @GetMapping("/oauth/{provider}")
    public ResponseEntity<String> oauthCallback(@PathVariable String provider, HttpServletResponse response,
                                                @RequestParam String code,
                                                @RequestParam(required = false) String state) {
        if (provider.equals("kakao")) {
            User loginUser = kakaoOauthService.login(new KakaoOauthLoginParam(provider, code));
            String accessToken = jwtService.generateTokenSubjectWithEmail(loginUser.getEmail());
            response.setHeader(HttpHeaders.AUTHORIZATION, accessToken);
            return ResponseEntity.ok().body("success");
        }
        if (provider.equals("naver")) {
            User loginUser = naverOauthService.login(new NaverOauthLoginParam(provider, code, state));
            String accessToken = jwtService.generateTokenSubjectWithEmail(loginUser.getEmail());
            response.setHeader(HttpHeaders.AUTHORIZATION, accessToken);
            return ResponseEntity.ok().body("success");
        }
        throw new IllegalArgumentException("no oauth provider!");
    }
}
