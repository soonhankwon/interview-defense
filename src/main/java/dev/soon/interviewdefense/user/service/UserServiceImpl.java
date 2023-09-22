package dev.soon.interviewdefense.user.service;

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
        User user = userRepository.findUserByEmail(securityUser.getUsername())
                        .orElseThrow(() -> new IllegalStateException());
        user.update(form);
    }

    @Override
    public User getLoginUserInfo(SecurityUser securityUser) {
        User user = userRepository.findUserByEmail(securityUser.getUsername())
                .orElseThrow(() -> new IllegalStateException());
        return user;
    }

    @Override
    public List<Language> getLoginUserLanguages(SecurityUser securityUser) {
        User user = userRepository.findUserByEmail(securityUser.getUsername())
                .orElseThrow(() -> new IllegalStateException());
        return languageRepository.findLanguagesByUser(user);
    }

    @Override
    public List<Tech> getLoginUserTechs(SecurityUser securityUser) {
        User user = userRepository.findUserByEmail(securityUser.getUsername())
                .orElseThrow(() -> new IllegalStateException());
        return techRepository.findTechesByUser(user);
    }

    @Override
    @Transactional
    public void addMyLanguageInMyPage(SecurityUser securityUser, MyLanguageReqDto dto) {
        User user = userRepository.findUserByEmail(securityUser.getUsername())
                .orElseThrow(() -> new IllegalStateException());
        if(languageRepository.existsLanguageByUserAndName(user, dto.name()))
            throw new IllegalArgumentException();
        user.addMyLanguage(dto);
    }

    @Override
    @Transactional
    public void deleteMyLanguageInMyPage(SecurityUser securityUser, Long languageId) {
        User user = userRepository.findUserByEmail(securityUser.getUsername())
                .orElseThrow(() -> new IllegalStateException());
        Language language = languageRepository.findLanguageByIdAndUser(languageId, user)
                .orElseThrow();
        languageRepository.delete(language);
    }

    @Override
    @Transactional
    public void addMyTechInMyPage(SecurityUser securityUser, MyTechReqDto dto) {
        User user = userRepository.findUserByEmail(securityUser.getUsername())
                .orElseThrow(() -> new IllegalStateException());
        if(techRepository.existsByUserAndName(user, dto.name()))
            throw new IllegalArgumentException();
        user.addMyTech(dto);
    }

    @Override
    @Transactional
    public void deleteMyTechInMyPage(SecurityUser securityUser, Long techId) {
        User user = userRepository.findUserByEmail(securityUser.getUsername())
                .orElseThrow(() -> new IllegalStateException());
        Tech tech = techRepository.findTechByIdAndUser(techId, user)
                .orElseThrow();
        techRepository.delete(tech);
    }
}
