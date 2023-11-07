package dev.soon.interviewdefense.chat.controller.dto;

import dev.soon.interviewdefense.chat.domain.ChatTopic;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "채팅 요청 DTO")
public record ChatRequest(
        @Schema(description = "채팅 주제", example = "JAVA")
        ChatTopic topic) {
}
