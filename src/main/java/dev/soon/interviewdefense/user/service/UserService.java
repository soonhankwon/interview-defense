package dev.soon.interviewdefense.user.service;

import dev.soon.interviewdefense.exception.ApiException;
import dev.soon.interviewdefense.exception.CustomErrorCode;
import dev.soon.interviewdefense.user.controller.dto.*;
import dev.soon.interviewdefense.user.domain.User;
import dev.soon.interviewdefense.user.domain.UserLanguage;
import dev.soon.interviewdefense.user.domain.UserTech;
import dev.soon.interviewdefense.user.repository.UserLanguageRepository;
import dev.soon.interviewdefense.user.repository.UserRepository;
import dev.soon.interviewdefense.user.repository.UserTechRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserLanguageRepository userLanguageRepository;
    private final UserTechRepository userTechRepository;

    public LoginUserResponse getUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_USER_IN_DB));
        return user.ofResponse();
    }

    @Transactional
    public String updateUserPosition(String email, UserPositionUpdateRequest dto) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_USER_IN_DB));
        user.updatePosition(dto);
        return "updated";
    }

    @Transactional
    public String addUserLanguages(String email, UserLanguageRequest dto) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_USER_IN_DB));
        user.addLanguages(dto);
        return "added";
    }

    @Transactional
    public String deleteUserLanguage(String email, UserLanguageDeleteRequest dto) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_USER_IN_DB));
        UserLanguage userLanguage = userLanguageRepository.findUserLanguageByUserAndLanguage(user, dto.language())
                .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_LANGUAGE_BY_USER));
        userLanguageRepository.delete(userLanguage);
        return "deleted";
    }

    @Transactional
    public String addUserTechs(String email, UserTechRequest dto) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_USER_IN_DB));
        user.addTechs(dto);
        return "added";
    }

    @Transactional
    public String deleteUserTech(String email, UserTechDeleteRequest dto) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_USER_IN_DB));
        UserTech userTech = userTechRepository.findUserTechByUserAndTech(user, dto.tech())
                .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_TECH_BY_USER));
        userTechRepository.delete(userTech);
        return "deleted";
    }

    @Transactional
    public String updateUserWorkExperienceYear(String email, UserWorkExperienceYearUpdateRequest dto) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_USER_IN_DB));
        user.updateWorkYear(dto);
        return "updated";
    }
}
