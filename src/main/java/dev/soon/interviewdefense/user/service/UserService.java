package dev.soon.interviewdefense.user.service;

import dev.soon.interviewdefense.user.domain.User;
import dev.soon.interviewdefense.user.domain.UserLanguage;
import dev.soon.interviewdefense.user.domain.UserTech;
import dev.soon.interviewdefense.web.dto.MyLanguageReqDto;
import dev.soon.interviewdefense.web.dto.MyPageUpdateForm;
import dev.soon.interviewdefense.web.dto.MyTechReqDto;

import java.util.List;

public interface UserService {
    void updateMyPage(String email, MyPageUpdateForm form);

    User getUserByEmail(String email);

    List<UserLanguage> getUserLanguages(String email);

    List<UserTech> getUserTechs(String email);

    void addMyLanguageInMyPage(String email, MyLanguageReqDto dto);

    void deleteMyLanguageInMyPage(String email, Long languageId);

    void addMyTechInMyPage(String email, MyTechReqDto dto);

    void deleteMyTechInMyPage(String email, Long techId);
}
