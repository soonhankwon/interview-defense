package dev.soon.interviewdefense.oauth.service;

import dev.soon.interviewdefense.oauth.controller.dto.NaverOauthLoginParam;
import dev.soon.interviewdefense.oauth.controller.dto.OauthTokenResDto;
import dev.soon.interviewdefense.oauth.domain.NaverUserInfo;
import dev.soon.interviewdefense.oauth.domain.Oauth2UserInfo;
import dev.soon.interviewdefense.user.domain.User;
import dev.soon.interviewdefense.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverOauthService implements OauthService<NaverOauthLoginParam> {
    private final UserRepository userRepository;
    private final InMemoryClientRegistrationRepository inMemoryRepository;

    @Transactional
    @Override
    public User login(NaverOauthLoginParam params) {

        ClientRegistration provider = inMemoryRepository.findByRegistrationId(params.provider());
        log.info("redirect uri = {}", provider.getRedirectUri());
        OauthTokenResDto tokenResponse = getToken(params.code(), provider, params.state());
        log.info("OauthAccessToken = {}", tokenResponse.access_token());
        return getUserFromNaver(params.provider(), tokenResponse, provider);
    }

    private OauthTokenResDto getToken(String code, ClientRegistration provider, String state) {
        return WebClient.create()
                .post()
                .uri(provider.getProviderDetails().getTokenUri())
                .headers(header -> {
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(requestToken(code, provider, state))
                .retrieve()
                .bodyToMono(OauthTokenResDto.class)
                .block();
    }

    private MultiValueMap<String, String> requestToken(String code, ClientRegistration provider, String state) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", provider.getRedirectUri());
        formData.add("client_secret", provider.getClientSecret());
        formData.add("client_id", provider.getClientId());
        formData.add("state", state);
        return formData;
    }

    private User getUserFromNaver(String providerName, OauthTokenResDto tokenResponse, ClientRegistration provider) {
        Map<String, Object> userAttributes = getUserAttribute(provider, tokenResponse);
        if (!providerName.equals("naver")) {
            throw new RuntimeException("invalid provider name");
        }

        Oauth2UserInfo oauth2UserInfo = new NaverUserInfo(userAttributes);
        if (oauth2UserInfo.getProvider() == null) {
            throw new RuntimeException("provider is null");
        }

        String email = oauth2UserInfo.getEmail();
        String nickname = oauth2UserInfo.getNickName();
        String oauth2Provider = oauth2UserInfo.getProvider();
        String imageUrl = oauth2UserInfo.getImageUrl();

        log.info("imageUrl={}", imageUrl);

        Optional<User> optionalUser = userRepository.findUserByEmail(email);

        if (optionalUser.isEmpty()) {
            User user = new User(email, nickname, oauth2Provider, imageUrl);
            userRepository.save(user);
            return user;
        }
        return optionalUser.get();
    }

    private Map<String, Object> getUserAttribute(ClientRegistration provider, OauthTokenResDto tokenResponse) {
        return WebClient.create()
                .get()
                .uri(provider.getProviderDetails().getUserInfoEndpoint().getUri())
                .headers(header -> header.setBearerAuth(String.valueOf(tokenResponse.access_token())))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .block();
    }
}
