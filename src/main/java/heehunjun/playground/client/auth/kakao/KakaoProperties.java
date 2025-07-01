package heehunjun.playground.client.auth.kakao;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "oauth.kakao")
public class KakaoProperties {

    private final String kakaoClientId;
    private final String kakaoAPIKey;
    private final String kakaoSecret;
    private final String TOKEN_URL;
    private final String REDIRECT_URL;
    private final String USER_INFO_URL;

    public KakaoProperties(String kakaoClientId, String kakaoAPIKey, String kakaoSecret,
                           String tokenUrl, String redirectUrl, String userInfoUrl) {
        this.kakaoClientId = kakaoClientId;
        this.kakaoAPIKey = kakaoAPIKey;
        this.kakaoSecret = kakaoSecret;
        TOKEN_URL = tokenUrl;
        REDIRECT_URL = redirectUrl;
        USER_INFO_URL = userInfoUrl;
    }
}
