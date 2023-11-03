package dev.soon.interviewdefense.user.controller.dto;

import dev.soon.interviewdefense.user.domain.*;

import java.util.List;

public record LoginUserResponse(
        String email,
        String nickname,
        String snsType,
        String imageUrl,
        Position position,
        Integer yearOfWorkExperience,
        List<Language> userLanguages,
        List<Tech> userTechs) {
}
