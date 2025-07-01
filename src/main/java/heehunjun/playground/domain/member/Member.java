package heehunjun.playground.domain.member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nickName;

    @Email
    private String email;

    @NotNull
    private String oauth;

    public Member(String nickName, String email, String oauth) {
        this.nickName = nickName;
        this.email = email;
        this.oauth = oauth;
    }
}
