package dev.soon.interviewdefense.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatMode {
    MENTOR("멘토링"), DEFENSE("디펜스");

    private final String value;
}
