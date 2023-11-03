package dev.soon.interviewdefense.chat.controller.dto;

import dev.soon.interviewdefense.chat.domain.ChatSender;

public record ChatMessageRecordResponse(Long id, ChatSender sender, String message) {
}
