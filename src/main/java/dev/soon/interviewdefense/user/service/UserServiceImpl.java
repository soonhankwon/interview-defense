package dev.soon.interviewdefense.user.service;

import dev.soon.interviewdefense.exception.ApiException;
import dev.soon.interviewdefense.exception.CustomErrorCode;
import dev.soon.interviewdefense.security.SecurityUser;
import dev.soon.interviewdefense.user.domain.UserLanguage;
import dev.soon.interviewdefense.user.domain.UserTech;
import dev.soon.interviewdefense.user.domain.User;
import dev.soon.interviewdefense.user.repository.UserLanguageRepository;
import dev.soon.interviewdefense.user.repository.UserTechRepository;
import dev.soon.interviewdefense.user.repository.UserRepository;
import dev.soon.interviewdefense.web.dto.MyLanguageReqDto;
import dev.soon.interviewdefense.web.dto.MyPageUpdateForm;
import dev.soon.interviewdefense.web.dto.MyTechReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserLanguageRepository userLanguageRepository;
    private final UserTechRepository userTechRepository;

    @Override
    @Transactional
    public void updateMyPage(SecurityUser securityUser, MyPageUpdateForm form) {
        User user = getUserBySecurityUser(securityUser);
        user.update(form);
    }

    @Override
    public User getLoginUserInfo(SecurityUser securityUser) {
        return getUserBySecurityUser(securityUser);
    }

    @Override
    public List<UserLanguage> getLoginUserLanguages(SecurityUser securityUser) {
        User user = getUserBySecurityUser(securityUser);
        return userLanguageRepository.findUserLanguagesByUser(user);
    }

    @Override
    public List<UserTech> getLoginUserTechs(SecurityUser securityUser) {
        User user = getUserBySecurityUser(securityUser);
        return userTechRepository.findUserTechesByUser(user);
    }

    @Override
    @Transactional
    public void addMyLanguageInMyPage(SecurityUser securityUser, MyLanguageReqDto dto) {
        User user = getUserBySecurityUser(securityUser);
        if(userLanguageRepository.existsUserLanguageByUserAndLanguage(user, dto.name()))
            throw new ApiException(CustomErrorCode.ALREADY_EXISTS_LANGUAGE_BY_USER);
        user.addMyLanguage(dto);
    }

    @Override
    @Transactional
    public void deleteMyLanguageInMyPage(SecurityUser securityUser, Long languageId) {
        User user = getUserBySecurityUser(securityUser);
        UserLanguage userLanguage = userLanguageRepository.findUserLanguageByIdAndUser(languageId, user)
                .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_LANGUAGE_BY_USER));
        userLanguageRepository.delete(userLanguage);
    }

    @Override
    @Transactional
    public void addMyTechInMyPage(SecurityUser securityUser, MyTechReqDto dto) {
        User user = getUserBySecurityUser(securityUser);
        if(userTechRepository.existsByUserAndTech(user, dto.name()))
            throw new ApiException(CustomErrorCode.ALREADY_EXISTS_TECH_BY_USER);
        user.addMyTech(dto);
    }

    @Override
    @Transactional
    public void deleteMyTechInMyPage(SecurityUser securityUser, Long techId) {
        User user = getUserBySecurityUser(securityUser);
        UserTech userTech = userTechRepository.findUserTechByIdAndUser(techId, user)
                .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_TECH_BY_USER));
        userTechRepository.delete(userTech);
    }

    private User getUserBySecurityUser(SecurityUser securityUser) {
        return userRepository.findUserByEmail(securityUser.getUsername())
                .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_USER_IN_DB));
    }
}
