package heehunjun.playground.domain.auth.oauth.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class KakaoProperties {

    private final String kakaoClientId = System.getenv("CLIENT_ID");
    private final String kakaoRestAPIKey = System.getenv("KAKAO_APIKEY");
    private final String kakaoSecret = System.getenv("CLIENT_SECRET_KEY");
    private final String TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private final String REDIRECT_URL = "http://localhost:8080/oauth2/kakao";
    private final String USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    // 넣기 귀찮아

    @Override
    public String toString() {
        return "KakaoProperties{" +
                "kakaoClientId='" + kakaoClientId + '\'' +
                ", kakaoRestAPIKey='" + kakaoRestAPIKey + '\'' +
                ", kakaoSecret='" + kakaoSecret + '\'' +
                ", TOKEN_URL='" + TOKEN_URL + '\'' +
                ", REDIRECT_URL='" + REDIRECT_URL + '\'' +
                ", USER_INFO_URL='" + USER_INFO_URL + '\'' +
                '}';
    }
}
