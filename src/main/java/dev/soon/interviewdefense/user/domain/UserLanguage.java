package dev.soon.interviewdefense.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "user_language", indexes = {
        @Index(name = "idx_language_idx", columnList = "language"),
        @Index(name = "fk_user_idx", columnList = "user_id")
})
public class UserLanguage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Language language;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public UserLanguage(User user, Language language) {
        this.user = user;
        this.language = language;
    }

    @Override
    public String toString() {
        return "UserLanguage{" +
                "language=" + language +
                '}';
    }
}
