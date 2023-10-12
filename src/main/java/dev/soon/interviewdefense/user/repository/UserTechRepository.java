package dev.soon.interviewdefense.user.repository;

import dev.soon.interviewdefense.user.domain.UserTech;
import dev.soon.interviewdefense.user.domain.Tech;
import dev.soon.interviewdefense.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTechRepository extends JpaRepository<UserTech, Long> {
    List<UserTech> findUserTechesByUser(User user);
    Optional<UserTech> findUserTechByIdAndUser(Long techId, User user);
    boolean existsByUserAndTech(User user, Tech tech);
}
