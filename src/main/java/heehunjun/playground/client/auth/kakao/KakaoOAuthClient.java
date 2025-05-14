package heehunjun.playground.client.auth.kakao;

import heehunjun.playground.client.auth.OAuthClient;
import heehunjun.playground.dto.member.MemberInfo;
import heehunjun.playground.dto.member.OauthToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Slf4j
@Component("kakao")
public class KakaoOAuthClient implements OAuthClient {

    private static final String OAUTH_GRANT_TYPE = "authorization_code";

    private final KakaoProperties kakaoProperties;
    private final RestClient restClient;

    public KakaoOAuthClient(RestClient.Builder restClientBuilder) {
        this.kakaoProperties = new KakaoProperties();
        this.restClient = restClientBuilder.build();
    }

    @Override
    public OauthToken getOauthToken(String code) {
        MultiValueMap<String, String> params = generateParams(code);

        return restClient.post()
                .uri(kakaoProperties.getTOKEN_URL())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(params)
                .retrieve()
//              .onStatus(HttpStatusCode::isError, new OAuthClient);
                .body(OauthToken.class);
    }

    private MultiValueMap<String, String> generateParams(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", kakaoProperties.getKakaoClientId());
        params.add("redirect_uri", kakaoProperties.getREDIRECT_URL());
        params.add("client_secret", kakaoProperties.getKakaoSecret());
        params.add("grant_type", OAUTH_GRANT_TYPE);
        return params;
    }

    @Override
    public MemberInfo getMemberInfo(String token) {
        return restClient.get()
                .uri(kakaoProperties.getUSER_INFO_URL())
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .body(MemberInfo.class);
    }
}
