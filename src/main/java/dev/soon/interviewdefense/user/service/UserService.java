package dev.soon.interviewdefense.user.service;

import dev.soon.interviewdefense.security.SecurityUser;
import dev.soon.interviewdefense.user.domain.User;
import dev.soon.interviewdefense.web.dto.MyPageUpdateForm;

public interface UserService {
    void updateMyPage(SecurityUser user, MyPageUpdateForm form);

    User getLoginUserInfo(SecurityUser securityUser);
}
