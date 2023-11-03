package dev.soon.interviewdefense.chat.domain;

import dev.soon.interviewdefense.chat.controller.dto.ChatMessageRecordResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@ToString
@Getter
@Entity
@Table(name = "chat_message", indexes = {
        @Index(name = "fk_chat_idx", columnList = "chat_id")
})
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

    private LocalDateTime createdAt;

    public ChatMessage(String message, Chat chat, ChatSender sender) {
        this.message = message;
        this.chat = chat;
        this.sender = sender;
        this.createdAt = LocalDateTime.now();
    }

    public ChatMessageRecordResponse ofResponse() {
        return new ChatMessageRecordResponse(this.id, this.sender, this.message);
    }
}
