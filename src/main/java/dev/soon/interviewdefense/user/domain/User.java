package dev.soon.interviewdefense.user.domain;

import dev.soon.interviewdefense.user.controller.dto.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@ToString
@Getter
@Entity
@Table(name = "`user`", indexes = {
        @Index(name = "idx_user_email_idx", columnList = "email")})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String nickname;

    private String snsType;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Position position;

    private Integer yearOfWorkExperience;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final Set<UserLanguage> userLanguages = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final Set<UserTech> userTechs = new HashSet<>();

    public User(String email, String nickname, String oauth2Provider, String imageUrl) {
        this.email = email;
        this.nickname = nickname;
        this.snsType = oauth2Provider;
        this.imageUrl = imageUrl;
        this.position = Position.DEFAULT;
        this.yearOfWorkExperience = 0;
    }

    public LoginUserResponse ofResponse() {
        return new LoginUserResponse(
                this.email,
                this.nickname,
                this.snsType,
                this.imageUrl,
                this.position,
                this.yearOfWorkExperience,
                this.userLanguages.stream().map(UserLanguage::getLanguage).collect(Collectors.toList()),
                this.userTechs.stream().map(UserTech::getTech).collect(Collectors.toList()));
    }

    public void updatePosition(UserPositionUpdateRequest dto) {
        this.position = dto.position();
    }

    public void addLanguages(UserLanguageRequest dto) {
        dto.languages().forEach(language -> {
            UserLanguage userLanguage = new UserLanguage(this, language);
            this.userLanguages.add(userLanguage);
        });
    }

    public void addTechs(UserTechRequest dto) {
        dto.techs().forEach(tech -> {
            UserTech userTech = new UserTech(this, tech);
            this.userTechs.add(userTech);
        });
    }

    public void updateWorkYear(UserWorkExperienceYearUpdateRequest dto) {
        if(dto.year() < 0) {
            throw new IllegalArgumentException("arg must over 0");
        }
        this.yearOfWorkExperience = dto.year();
    }
}