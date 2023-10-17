package dev.soon.interviewdefense.chat.event;

import dev.soon.interviewdefense.chat.domain.ChatMessage;

public record MessageSendEvent(ChatMessage chatMessage) {
}