package dev.soon.interviewdefense.chat.controller.dto;

import dev.soon.interviewdefense.chat.domain.ChatTopic;

import java.time.LocalDateTime;

public record ChatResponse(
        ChatTopic topic,
        LocalDateTime createdAt,
        Long id) {
}
