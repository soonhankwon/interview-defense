package dev.soon.interviewdefense.oauth.controller;

import dev.soon.interviewdefense.oauth.controller.dto.KakaoOauthLoginParam;
import dev.soon.interviewdefense.oauth.controller.dto.NaverOauthLoginParam;
import dev.soon.interviewdefense.oauth.service.OauthService;
import dev.soon.interviewdefense.security.service.JwtService;
import dev.soon.interviewdefense.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
@Controller
public class OauthController {

    private final OauthService<KakaoOauthLoginParam> kakaoOauthService;
    private final OauthService<NaverOauthLoginParam> naverOauthService;
    private final JwtService jwtService;

    @GetMapping("/oauth/{provider}")
    public String oauthCallback(@PathVariable String provider, HttpServletResponse response,
                                @RequestParam String code, @RequestParam(required = false) String state,
                                @RequestParam(defaultValue = "/") String redirectUrl) {
        log.info("code={}", code);
        if(provider.equals("kakao")) {
            User loginUser = kakaoOauthService.login(new KakaoOauthLoginParam(provider, code));
            String accessToken = jwtService.generateTokenSubjectWithEmail(loginUser.getEmail());
            Cookie cookie = new Cookie("AccessToken", accessToken);
            cookie.setPath("/");
            response.addCookie(cookie);
            log.info("cookie={}", cookie.getValue());
            return "redirect:" + redirectUrl;
        }
        if(provider.equals("naver")) {
            User loginUser = naverOauthService.login(new NaverOauthLoginParam(provider, code, state));
            String accessToken = jwtService.generateTokenSubjectWithEmail(loginUser.getEmail());
            Cookie cookie = new Cookie("AccessToken", accessToken);
            cookie.setPath("/");
            response.addCookie(cookie);
            log.info("cookie={}", cookie.getValue());
            return "redirect:" + redirectUrl;
        }
        return "redirect:/";
    }
}
