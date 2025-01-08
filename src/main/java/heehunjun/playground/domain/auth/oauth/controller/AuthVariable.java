package heehunjun.playground.domain.auth.oauth.controller;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AuthVariable {

    private final String kakaoClientId = System.getenv("CLIENT_ID");
    private final String kakaoRestAPIKey = System.getenv("KAKAO_APIKEY");
    private final String kakaoSecret = System.getenv("CLIENT_SECRET_KEY");

    @Override
    public String toString() {
        return "AuthVariable{" +
                "kakaoClientId='" + kakaoClientId + '\'' +
                ", kakaoRestAPIKey='" + kakaoRestAPIKey + '\'' +
                ", kakaoSecret='" + kakaoSecret + '\'' +
                '}';
    }
}
