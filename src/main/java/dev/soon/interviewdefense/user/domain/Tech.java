package dev.soon.interviewdefense.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@NoArgsConstructor
@ToString
@Getter
@Entity
@Table(name = "tech", indexes = {
        @Index(name = "idx_tech_name_idx", columnList = "name"),
        @Index(name = "fk_user_idx", columnList = "user_id")
})
public class Tech {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TechName name;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Tech(User user, TechName name) {
        this.user = user;
        this.name = name;
    }
}
