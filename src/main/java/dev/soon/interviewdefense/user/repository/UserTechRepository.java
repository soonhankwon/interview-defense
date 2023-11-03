package dev.soon.interviewdefense.user.repository;

import dev.soon.interviewdefense.user.domain.Tech;
import dev.soon.interviewdefense.user.domain.User;
import dev.soon.interviewdefense.user.domain.UserTech;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTechRepository extends JpaRepository<UserTech, Long> {
    Optional<UserTech> findUserTechByUserAndTech(User user, Tech tech);
}
