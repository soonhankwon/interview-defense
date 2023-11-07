package dev.soon.interviewdefense.user.controller.dto;

import dev.soon.interviewdefense.user.domain.Language;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "유저 언어 삭제 요청 DTO")
public record UserLanguageDeleteRequest(
        @Schema(description = "삭제 언어", example = "JAVA")
        Language language) {
}
