package dev.soon.interviewdefense.chat.respository;

import dev.soon.interviewdefense.chat.domain.Chat;
import dev.soon.interviewdefense.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findChatsByUser(User user);

    Optional<Chat> findChatByIdAndUser(Long chatRoomId, User user);
}
