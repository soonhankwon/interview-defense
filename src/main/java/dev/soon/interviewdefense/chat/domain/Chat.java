package dev.soon.interviewdefense.chat.domain;

import dev.soon.interviewdefense.chat.controller.dto.ChatRoomResponse;
import dev.soon.interviewdefense.chat.controller.dto.ChatRoomRequest;
import dev.soon.interviewdefense.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@ToString
@Getter
@Entity
@Table(name = "chat", indexes = {
        @Index(name = "fk_user_idx", columnList = "user_id")
})
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ChatTopic topic;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private LocalDateTime createAt;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ChatMessage> chatMessages = new ArrayList<>();

    public Chat(ChatRoomRequest dto, User user) {
        this.topic = dto.topic();
        this.user = user;
        this.createAt = LocalDateTime.now();
    }

    public ChatRoomResponse ofResponse() {
        return new ChatRoomResponse(this.topic, this.createAt, this.id);
    }
}