package dev.soon.interviewdefense.user.repository;

import dev.soon.interviewdefense.user.domain.Language;
import dev.soon.interviewdefense.user.domain.LanguageName;
import dev.soon.interviewdefense.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
    List<Language> findLanguagesByUser(User user);
    Optional<Language> findLanguageByIdAndUser(Long languageId, User user);
    boolean existsLanguageByUserAndName(User user, LanguageName name);
}
