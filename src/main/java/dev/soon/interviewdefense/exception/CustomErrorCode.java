package dev.soon.interviewdefense.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CustomErrorCode {
    NOT_EXISTS_USER_IN_DB("유저정보가 DB에 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    NOT_EXISTS_CHATROOM_IN_DB("채팅방정보가 DB에 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    NOT_MATCHES_CHAT_ID_AND_USER("채팅방 ID와 유저정보가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    NOT_EXISTS_LATEST_CHAT_MESSAGE("채팅방에 최신 메세지가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    INVALID_FLAG_IN_FRONT("프론트의 Flag 정보가 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    NOT_EXISTS_TECH_BY_USER("유저의 기술스택 정보가 없습니다.", HttpStatus.NOT_FOUND),
    ALREADY_EXISTS_TECH_BY_USER("이미 유저의 기술스택이 추가되어 있습니다.", HttpStatus.BAD_REQUEST),
    NOT_EXISTS_LANGUAGE_BY_USER("유저의 언어 정보가 없습니다.", HttpStatus.NOT_FOUND),
    ALREADY_EXISTS_LANGUAGE_BY_USER("이미 유저의 언어가 추가되어 있습니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;
}
