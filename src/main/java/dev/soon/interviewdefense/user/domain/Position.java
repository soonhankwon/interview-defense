package dev.soon.interviewdefense.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Position {
    FULLSTACK_DEVELOPER("풀스택 개발자"),
    BACKEND_DEVELOPER("백엔드 개발자"),
    FRONTEND_DEVELOPER("프론트엔드 개발자"),
    DEV_OPS("데브옵스"),
    STUDENT("학생"),
    DEFAULT("기타");

    private final String value;
}
