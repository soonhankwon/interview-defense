package dev.soon.interviewdefense.chat.respository;

import dev.soon.interviewdefense.chat.domain.Chat;
import dev.soon.interviewdefense.chat.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findChatMessagesByChat(Chat chat);

    Optional<ChatMessage> findTopByChatOrderByCreatedAtDesc(Chat chat);
}
