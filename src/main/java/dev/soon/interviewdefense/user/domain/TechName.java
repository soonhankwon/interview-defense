package dev.soon.interviewdefense.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TechName {
    SPRING("스프링"),
    SPRING_BOOT("스프링부트"),
    KAFKA("카프카"),
    REACT("리액트"),
    JPA("JPA"),
    MY_SQL("MySQL"),
    MONGO_DB("MongoDB");

    private final String value;
}
