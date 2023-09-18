package dev.soon.interviewdefense.user.domain;

import dev.soon.interviewdefense.web.dto.MyPageUpdateForm;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@NoArgsConstructor
@ToString
@Getter
@Entity
@Table(name = "`user`")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String nickname;

    private String snsType;

    private String imageUrl;

    private String position;

    private Integer yearOfWorkExperience;

    public User(String email, String nickname, String oauth2Provider, String imageUrl) {
        this.email = email;
        this.nickname = nickname;
        this.snsType = oauth2Provider;
        this.imageUrl = imageUrl;
        this.position = "default";
        this.yearOfWorkExperience = 0;
    }

    public void update(MyPageUpdateForm form) {
        this.nickname = form.nickname();
        this.position = form.position();
        this.yearOfWorkExperience = form.yearOfWorkExperience();
    }
}
