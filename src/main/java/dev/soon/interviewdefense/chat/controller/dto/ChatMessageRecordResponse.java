package dev.soon.interviewdefense.chat.controller.dto;

import dev.soon.interviewdefense.chat.domain.ChatSender;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "채팅 메세지 응답 DTO")
public record ChatMessageRecordResponse(
        @Schema(description = "채팅 ID", example = "1")
        Long id,
        @Schema(description = "메세지 발신자", example = "AI")
        ChatSender sender,
        @Schema(description = "메세지", example = "DI란?")
        String message) {
}
