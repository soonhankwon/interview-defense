package dev.soon.interviewdefense.defense.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.ThreadLocalRandom;

@Getter
@AllArgsConstructor
public enum DefenseLength {
    SHORT("10~20"),
    MIDDLE("20~30"),
    LONG("30~40");

    private final String value;

    public Integer executeRandomCounter() {
        if(this == SHORT)
            return ThreadLocalRandom.current().nextInt(10, 21);
        if(this == MIDDLE)
            return ThreadLocalRandom.current().nextInt(20, 31);
        else
            return ThreadLocalRandom.current().nextInt(30, 41);
    }
}
