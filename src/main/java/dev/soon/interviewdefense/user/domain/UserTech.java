package dev.soon.interviewdefense.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@NoArgsConstructor
@ToString
@Getter
@Entity
@Table(name = "user_tech", indexes = {
        @Index(name = "idx_tech_idx", columnList = "tech"),
        @Index(name = "fk_user_idx", columnList = "user_id")
})
public class UserTech {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Tech tech;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public UserTech(User user, Tech tech) {
        this.user = user;
        this.tech = tech;
    }
}
