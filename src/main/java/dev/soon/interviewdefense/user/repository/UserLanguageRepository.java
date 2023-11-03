package dev.soon.interviewdefense.user.repository;

import dev.soon.interviewdefense.user.domain.Language;
import dev.soon.interviewdefense.user.domain.User;
import dev.soon.interviewdefense.user.domain.UserLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLanguageRepository extends JpaRepository<UserLanguage, Long> {
    Optional<UserLanguage> findUserLanguageByUserAndLanguage(User user, Language language);
}
