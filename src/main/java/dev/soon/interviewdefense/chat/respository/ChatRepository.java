package dev.soon.interviewdefense.chat.respository;

import dev.soon.interviewdefense.chat.domain.Chat;
import dev.soon.interviewdefense.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findChatsByUser(User user);
}
