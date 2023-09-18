package dev.soon.interviewdefense.oauth.domain;

public interface Oauth2UserInfo {
    String getProvider();
    String getEmail();
    String getNickName();
    String getImageUrl();
}
