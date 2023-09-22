package dev.soon.interviewdefense.user.repository;

import dev.soon.interviewdefense.user.domain.Tech;
import dev.soon.interviewdefense.user.domain.TechName;
import dev.soon.interviewdefense.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TechRepository extends JpaRepository<Tech, Long> {
    List<Tech> findTechesByUser(User user);
    Optional<Tech> findTechByIdAndUser(Long techId, User user);
    boolean existsByUserAndName(User user, TechName name);
}
