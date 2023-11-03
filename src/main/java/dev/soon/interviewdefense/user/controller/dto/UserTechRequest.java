package dev.soon.interviewdefense.user.controller.dto;

import dev.soon.interviewdefense.user.domain.Tech;

import java.util.List;

public record UserTechRequest(List<Tech> techs) {
}
