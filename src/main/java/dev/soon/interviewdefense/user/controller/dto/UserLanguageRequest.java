package dev.soon.interviewdefense.user.controller.dto;

import dev.soon.interviewdefense.user.domain.Language;

import java.util.List;

public record UserLanguageRequest(List<Language> languages) {
}
