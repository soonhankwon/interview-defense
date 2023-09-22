package dev.soon.interviewdefense.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@NoArgsConstructor
@ToString
@Getter
@Entity
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private LanguageName name;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Language(User user, LanguageName name) {
        this.user = user;
        this.name = name;
    }
}
