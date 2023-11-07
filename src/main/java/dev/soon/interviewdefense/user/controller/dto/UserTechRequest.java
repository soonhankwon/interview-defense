package dev.soon.interviewdefense.user.controller.dto;

import dev.soon.interviewdefense.user.domain.Tech;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "유저 기술 추가 및 변경 요청 DTO")
public record UserTechRequest(
        @Schema(description = "기술 목록", example = "[SPRING, REACT]")
        List<Tech> techs) {
}
