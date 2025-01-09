package heehunjun.playground.domain.auth.oauth.client;

import heehunjun.playground.domain.auth.oauth.config.KakaoProperties;
import heehunjun.playground.domain.auth.oauth.service.kakao.KakaoTokenResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

// todo : restTemplate 의존성 주입 , 외부 api 예외 처리 ? ?
@Slf4j
@Component
public class KakaoOAuthClient {

    private static final String OAUTH_GRANT_TYPE = "authorization_code";

    private final KakaoProperties kakaoProperties;
    private final RestTemplate restTemplate;

    public KakaoOAuthClient(final KakaoProperties kakaoProperties,final RestTemplateBuilder restTemplateBuilder) {
        this.kakaoProperties = kakaoProperties;
        this.restTemplate = restTemplateBuilder.build();
    }
    /**
     * 구글 OAuth 는 Access Token 을 받아오면서 IdToken 도 받아와서 거기서 필요한 정보는 다 가져옴 (한번 더 Access Token으로 요청도 가능)
     * 근데, Kakao 는 Access Token 을 받고 한번 더 해당 토큰과 요청 해야 해
     * -> getUserInfo 추가됨
     * 제네릭 사용해서 GoogleClient 와 추상화 ? ? ?
     */
    public KakaoTokenResponse getKakaoAccessToken(final String code) {

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        final MultiValueMap<String, String> params = generateParams(code);
        final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        final ResponseEntity<KakaoTokenResponse> response = restTemplate
                .postForEntity(kakaoProperties.getTOKEN_URL(), request, KakaoTokenResponse.class);
        return response.getBody();
    }

    public Map<String, Object> getUserInfo(String accessToken) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        final HttpEntity<Void> request = new HttpEntity<>(headers);

        final ResponseEntity<Map> response = restTemplate.exchange(
                kakaoProperties.getUSER_INFO_URL(),
                org.springframework.http.HttpMethod.GET,
                request,
                Map.class
        );
        return response.getBody();
    }

    private MultiValueMap<String, String> generateParams(String code) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", kakaoProperties.getKakaoClientId());
        params.add("redirect_uri", kakaoProperties.getREDIRECT_URL());
        params.add("client_secret", kakaoProperties.getKakaoSecret());
        params.add("grant_type", OAUTH_GRANT_TYPE);
        return params;
    }
}
