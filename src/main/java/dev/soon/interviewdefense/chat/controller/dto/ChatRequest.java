package dev.soon.interviewdefense.chat.controller.dto;

import dev.soon.interviewdefense.chat.domain.ChatTopic;

public record ChatRequest(ChatTopic topic) {
}
