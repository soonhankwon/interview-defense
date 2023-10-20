package dev.soon.interviewdefense.user.service;

import dev.soon.interviewdefense.exception.ApiException;
import dev.soon.interviewdefense.exception.CustomErrorCode;
import dev.soon.interviewdefense.user.domain.User;
import dev.soon.interviewdefense.user.domain.UserLanguage;
import dev.soon.interviewdefense.user.domain.UserTech;
import dev.soon.interviewdefense.user.repository.UserLanguageRepository;
import dev.soon.interviewdefense.user.repository.UserRepository;
import dev.soon.interviewdefense.user.repository.UserTechRepository;
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
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserLanguageRepository userLanguageRepository;
    private final UserTechRepository userTechRepository;

    @Override
    @Transactional
    public void updateMyPage(String email, MyPageUpdateForm form) {
        User user = getUserByEmail(email);
        user.update(form);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_USER_IN_DB));
    }

    @Override
    public List<UserLanguage> getUserLanguages(String email) {
        User user = getUserByEmail(email);
        return userLanguageRepository.findUserLanguagesByUser(user);
    }

    @Override
    public List<UserTech> getUserTechs(String email) {
        User user = getUserByEmail(email);
        return userTechRepository.findUserTechesByUser(user);
    }

    @Override
    @Transactional
    public void addMyLanguageInMyPage(String email, MyLanguageReqDto dto) {
        User user = getUserByEmail(email);
        if (userLanguageRepository.existsUserLanguageByUserAndLanguage(user, dto.name()))
            throw new ApiException(CustomErrorCode.ALREADY_EXISTS_LANGUAGE_BY_USER);
        user.addMyLanguage(dto);
    }

    @Override
    @Transactional
    public void deleteMyLanguageInMyPage(String email, Long languageId) {
        User user = getUserByEmail(email);
        UserLanguage userLanguage = userLanguageRepository.findUserLanguageByIdAndUser(languageId, user)
                .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_LANGUAGE_BY_USER));
        userLanguageRepository.delete(userLanguage);
    }

    @Override
    @Transactional
    public void addMyTechInMyPage(String email, MyTechReqDto dto) {
        User user = getUserByEmail(email);
        if (userTechRepository.existsByUserAndTech(user, dto.name()))
            throw new ApiException(CustomErrorCode.ALREADY_EXISTS_TECH_BY_USER);
        user.addMyTech(dto);
    }

    @Override
    @Transactional
    public void deleteMyTechInMyPage(String email, Long techId) {
        User user = getUserByEmail(email);
        UserTech userTech = userTechRepository.findUserTechByIdAndUser(techId, user)
                .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_TECH_BY_USER));
        userTechRepository.delete(userTech);
    }
}
