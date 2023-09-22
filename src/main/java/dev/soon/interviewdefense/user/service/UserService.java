package dev.soon.interviewdefense.user.service;

import dev.soon.interviewdefense.security.SecurityUser;
import dev.soon.interviewdefense.user.domain.Language;
import dev.soon.interviewdefense.user.domain.Tech;
import dev.soon.interviewdefense.user.domain.User;
import dev.soon.interviewdefense.web.dto.MyLanguageReqDto;
import dev.soon.interviewdefense.web.dto.MyPageUpdateForm;
import dev.soon.interviewdefense.web.dto.MyTechReqDto;

import java.util.List;

public interface UserService {
    void updateMyPage(SecurityUser user, MyPageUpdateForm form);

    User getLoginUserInfo(SecurityUser securityUser);

    List<Language> getLoginUserLanguages(SecurityUser securityUser);

    List<Tech> getLoginUserTechs(SecurityUser securityUser);

    void addMyLanguageInMyPage(SecurityUser securityUser, MyLanguageReqDto dto);
    void deleteMyLanguageInMyPage(SecurityUser securityUser, Long languageId);

    void addMyTechInMyPage(SecurityUser securityUser, MyTechReqDto dto);
    void deleteMyTechInMyPage(SecurityUser securityUser, Long techId);
}
