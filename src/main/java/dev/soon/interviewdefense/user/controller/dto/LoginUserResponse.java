package dev.soon.interviewdefense.user.controller.dto;

import dev.soon.interviewdefense.user.domain.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "유저 정보 응답 DTO")
public record LoginUserResponse(
        @Schema(description = "이메일", example = "test@kakao.com")
        String email,
        @Schema(description = "닉네임", example = "test")
        String nickname,
        @Schema(description = "SNS", example = "KAKAO")
        String snsType,
        @Schema(description = "이미지 URL", example = "http://k.kakaocdn.net/xxxx")
        String imageUrl,
        @Schema(description = "포지션", example = "BACKEND_DEVELOPER")
        Position position,
        @Schema(description = "경력", example = "1")
        Integer yearOfWorkExperience,
        @Schema(description = "유저 언어 목록", example = "[JAVA, KOTLIN]")
        List<Language> userLanguages,
        @Schema(description = "유저 기술 목록", example = "[SPRING]")
        List<Tech> userTechs) {
}
