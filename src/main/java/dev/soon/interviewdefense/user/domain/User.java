package dev.soon.interviewdefense.user.domain;

import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@NoArgsConstructor
@ToString
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

    public User(String email, String nickname, String oauth2Provider, String imageUrl) {
        this.email = email;
        this.nickname = nickname;
        this.snsType = oauth2Provider;
        this.imageUrl = imageUrl;
    }

    public String getEmail() {
        return this.email;
    }
}
