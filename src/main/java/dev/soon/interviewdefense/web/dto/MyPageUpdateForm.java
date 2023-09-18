package dev.soon.interviewdefense.web.dto;

import dev.soon.interviewdefense.user.domain.Position;

public record MyPageUpdateForm(
        String nickname,
        Position position,
        Integer yearOfWorkExperience) {
}
