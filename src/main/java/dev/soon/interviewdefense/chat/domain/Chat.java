package dev.soon.interviewdefense.chat.domain;

import dev.soon.interviewdefense.chat.controller.dto.ChatResponse;
import dev.soon.interviewdefense.chat.controller.dto.ChatRequest;
import dev.soon.interviewdefense.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public Chat(ChatRequest dto, User user) {
        this.topic = dto.topic();
        this.user = user;
        this.createAt = LocalDateTime.now();
    }

    public ChatResponse ofResponse() {
        return new ChatResponse(
                this.topic,
                this.createAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm")),
                this.id);
    }
}