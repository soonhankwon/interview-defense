package dev.soon.interviewdefense.user.service;

import dev.soon.interviewdefense.exception.ApiException;
import dev.soon.interviewdefense.exception.CustomErrorCode;
import dev.soon.interviewdefense.security.SecurityUser;
import dev.soon.interviewdefense.user.domain.Language;
import dev.soon.interviewdefense.user.domain.Tech;
import dev.soon.interviewdefense.user.domain.User;
import dev.soon.interviewdefense.user.repository.LanguageRepository;
import dev.soon.interviewdefense.user.repository.TechRepository;
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
    private final LanguageRepository languageRepository;
    private final TechRepository techRepository;

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
    public List<Language> getLoginUserLanguages(SecurityUser securityUser) {
        User user = getUserBySecurityUser(securityUser);
        return languageRepository.findLanguagesByUser(user);
    }

    @Override
    public List<Tech> getLoginUserTechs(SecurityUser securityUser) {
        User user = getUserBySecurityUser(securityUser);
        return techRepository.findTechesByUser(user);
    }

    @Override
    @Transactional
    public void addMyLanguageInMyPage(SecurityUser securityUser, MyLanguageReqDto dto) {
        User user = getUserBySecurityUser(securityUser);
        if(languageRepository.existsLanguageByUserAndName(user, dto.name()))
            throw new ApiException(CustomErrorCode.ALREADY_EXISTS_LANGUAGE_BY_USER);
        user.addMyLanguage(dto);
    }

    @Override
    @Transactional
    public void deleteMyLanguageInMyPage(SecurityUser securityUser, Long languageId) {
        User user = getUserBySecurityUser(securityUser);
        Language language = languageRepository.findLanguageByIdAndUser(languageId, user)
                .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_LANGUAGE_BY_USER));
        languageRepository.delete(language);
    }

    @Override
    @Transactional
    public void addMyTechInMyPage(SecurityUser securityUser, MyTechReqDto dto) {
        User user = getUserBySecurityUser(securityUser);
        if(techRepository.existsByUserAndName(user, dto.name()))
            throw new ApiException(CustomErrorCode.ALREADY_EXISTS_TECH_BY_USER);
        user.addMyTech(dto);
    }

    @Override
    @Transactional
    public void deleteMyTechInMyPage(SecurityUser securityUser, Long techId) {
        User user = getUserBySecurityUser(securityUser);
        Tech tech = techRepository.findTechByIdAndUser(techId, user)
                .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_TECH_BY_USER));
        techRepository.delete(tech);
    }

    private User getUserBySecurityUser(SecurityUser securityUser) {
        return userRepository.findUserByEmail(securityUser.getUsername())
                .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_USER_IN_DB));
    }
}
