package dev.soon.interviewdefense.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatTopic {
    DEFAULT("기본"),
    JAVA("자바"),
    JAVASCRIPT("자바스크립트"),
    KOTLIN("코틀린"),
    REACT("리액트"),
    NEXT_JS("Nest.jS"),
    NODE_JS("Node.js"),
    NEST_JS("Nest.js"),
    SPRING("스프링"),
    CS("컴퓨터 사이언스");

    private final String value;
}
