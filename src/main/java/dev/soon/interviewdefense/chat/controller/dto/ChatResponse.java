package dev.soon.interviewdefense.chat.controller.dto;

import dev.soon.interviewdefense.chat.domain.ChatTopic;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "채팅 응답 DTO")
public record ChatResponse(
        @Schema(description = "채팅주제", example = "JAVA")
        ChatTopic topic,
        @Schema(description = "채팅 생성일시", example = "202311071305")
        String createdAt,
        @Schema(description = "채팅 ID", example = "1")
        Long id) {
}
