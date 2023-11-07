package dev.soon.interviewdefense.user.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "유저 경력 변경 요청 DTO")
public record UserWorkExperienceYearUpdateRequest(
        @Schema(description = "변경 요청 경력년수", example = "1")
        Integer year) {
}
