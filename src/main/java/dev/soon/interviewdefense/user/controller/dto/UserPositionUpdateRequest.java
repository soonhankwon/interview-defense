package dev.soon.interviewdefense.user.controller.dto;

import dev.soon.interviewdefense.user.domain.Position;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "유저 포지션 변경 요청 DTO")
public record UserPositionUpdateRequest(
        @Schema(description = "변경 요청 포지션", example = "BACKEND_DEVELOPER")
        Position position) {
}
