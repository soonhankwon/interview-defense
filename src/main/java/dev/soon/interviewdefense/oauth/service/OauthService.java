package dev.soon.interviewdefense.oauth.service;

import dev.soon.interviewdefense.user.domain.User;

public interface OauthService<T> {
    User login(T param);
}
