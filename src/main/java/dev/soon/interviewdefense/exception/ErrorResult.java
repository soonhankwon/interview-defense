package dev.soon.interviewdefense.exception;

import org.springframework.http.HttpStatus;

public record ErrorResult(
        HttpStatus code,
        String message
) {
}
