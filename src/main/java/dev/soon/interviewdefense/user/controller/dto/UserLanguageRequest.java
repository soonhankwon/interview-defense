package dev.soon.interviewdefense.user.controller.dto;

import dev.soon.interviewdefense.user.domain.Language;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "유저 언어 추가 및 변경 요청 DTO")
public record UserLanguageRequest(
        @Schema(description = "언어 목록", example = "[JAVA, KOTLIN]")
        List<Language> languages) {
}
