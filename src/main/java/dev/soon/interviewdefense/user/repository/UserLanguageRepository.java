package dev.soon.interviewdefense.user.repository;

import dev.soon.interviewdefense.user.domain.UserLanguage;
import dev.soon.interviewdefense.user.domain.Language;
import dev.soon.interviewdefense.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserLanguageRepository extends JpaRepository<UserLanguage, Long> {
    List<UserLanguage> findUserLanguagesByUser(User user);
    Optional<UserLanguage> findUserLanguageByIdAndUser(Long languageId, User user);
    boolean existsUserLanguageByUserAndLanguage(User user, Language language);
}
