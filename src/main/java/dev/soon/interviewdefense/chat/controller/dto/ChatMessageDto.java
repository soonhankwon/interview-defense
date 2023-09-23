package dev.soon.interviewdefense.chat.controller.dto;

import javax.validation.constraints.NotBlank;

public record ChatMessageDto(@NotBlank String message) {
}
