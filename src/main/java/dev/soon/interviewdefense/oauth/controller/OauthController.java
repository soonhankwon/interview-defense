package dev.soon.interviewdefense.oauth.controller;

import dev.soon.interviewdefense.oauth.controller.dto.KakaoOauthLoginParam;
import dev.soon.interviewdefense.oauth.controller.dto.NaverOauthLoginParam;
import dev.soon.interviewdefense.oauth.service.OauthService;
import dev.soon.interviewdefense.security.service.JwtService;
import dev.soon.interviewdefense.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class OauthController {

    private final OauthService<KakaoOauthLoginParam> kakaoOauthService;
    private final OauthService<NaverOauthLoginParam> naverOauthService;
    private final JwtService jwtService;

    @GetMapping("/oauth/{provider}")
    public ResponseEntity<String> oauthCallback(@PathVariable String provider, HttpServletResponse response,
                                                @RequestParam String code,
                                                @RequestParam(required = false) String state) throws IOException {
        if (provider.equals("kakao")) {
            User loginUser = kakaoOauthService.login(new KakaoOauthLoginParam(provider, code));
            String accessToken = jwtService.generateTokenSubjectWithEmail(loginUser.getEmail());
            Cookie cookie = new Cookie("AccessToken", accessToken);
            cookie.setPath("/");
            response.addCookie(cookie);
            response.sendRedirect("http://localhost:3000/login-home");
            return ResponseEntity.ok().body("success");
        }
        if (provider.equals("naver")) {
            User loginUser = naverOauthService.login(new NaverOauthLoginParam(provider, code, state));
            String accessToken = jwtService.generateTokenSubjectWithEmail(loginUser.getEmail());
            Cookie cookie = new Cookie("AccessToken", accessToken);
            cookie.setPath("/");
            response.addCookie(cookie);
            return ResponseEntity.ok().body("success");
        }
        throw new IllegalArgumentException("no oauth provider!");
    }
}
