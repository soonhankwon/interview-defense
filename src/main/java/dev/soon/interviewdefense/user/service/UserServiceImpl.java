package dev.soon.interviewdefense.user.service;

import dev.soon.interviewdefense.security.SecurityUser;
import dev.soon.interviewdefense.user.domain.User;
import dev.soon.interviewdefense.user.repository.UserRepository;
import dev.soon.interviewdefense.web.dto.MyPageUpdateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

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
}
