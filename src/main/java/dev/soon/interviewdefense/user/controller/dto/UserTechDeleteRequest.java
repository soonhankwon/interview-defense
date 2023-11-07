package dev.soon.interviewdefense.user.controller.dto;

import dev.soon.interviewdefense.user.domain.Tech;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "유저 기술 삭제 요청 DTO")
public record UserTechDeleteRequest(
        @Schema(description = "삭제 요청 기술", example = "REACT")
        Tech tech) {
}
