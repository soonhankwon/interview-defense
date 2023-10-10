package dev.soon.interviewdefense.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@NoArgsConstructor
@ToString
@Getter
@Entity
@Table(name = "language", indexes = {
        @Index(name = "idx_language_name_idx", columnList = "name"),
        @Index(name = "fk_user_idx", columnList = "user_id")
})
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
