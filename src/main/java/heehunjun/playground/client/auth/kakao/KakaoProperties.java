package heehunjun.playground.client.auth.kakao;

import lombok.Getter;

@Getter
public class KakaoProperties {

    private final String kakaoClientId = System.getenv("CLIENT_ID");
    private final String kakaoRestAPIKey = System.getenv("KAKAO_APIKEY");
    private final String kakaoSecret = System.getenv("CLIENT_SECRET_KEY");
    private final String TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private final String REDIRECT_URL = "http://localhost:8080/oauth2/kakao";
    private final String USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";
}
