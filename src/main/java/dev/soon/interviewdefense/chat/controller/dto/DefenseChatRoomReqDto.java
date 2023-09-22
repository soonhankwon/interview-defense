package dev.soon.interviewdefense.chat.controller.dto;

import dev.soon.interviewdefense.chat.domain.ChatTopic;
import dev.soon.interviewdefense.defense.domain.DefenseLength;

public record DefenseChatRoomReqDto(ChatTopic topic, DefenseLength length) {
}
