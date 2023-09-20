package dev.soon.interviewdefense.chat.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@ToString
@Getter
@Entity
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    private Chat chat;

    @Enumerated(EnumType.STRING)
    private ChatSender sender;

    private LocalDateTime createAt;

    public ChatMessage(String message, Chat chat, ChatSender sender) {
        this.message = message;
        this.chat = chat;
        this.sender = sender;
        this.createAt = LocalDateTime.now();
    }
}
