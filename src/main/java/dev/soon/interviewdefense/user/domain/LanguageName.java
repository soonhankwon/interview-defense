package dev.soon.interviewdefense.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LanguageName {
    JAVA("자바"),
    PYTHON("파이썬"),
    JAVASCRIPT("자바스크립트"),
    GOLANG("GO"),
    C("C"),
    C_PLUS_PLUS("C++"),
    C_SHARP("C#");

    private final String value;
}
