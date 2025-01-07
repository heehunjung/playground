package heehunjun.playground.global.auth.service;

import heehunjun.playground.domain.member.domain.Member;
import heehunjun.playground.domain.member.domain.MemberRepository;
import heehunjun.playground.global.auth.controller.AuthVariable;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoOauthService {

    private static final String TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";
    private static final String REDIRECT_URL = "http://localhost:8080/oauth2/kakao";

    private final AuthVariable authVariable;
    private final MemberRepository memberRepository;

    public void saveNewMember(Map<String, Object> userInfo) {
        // 3. 사용자 정보 반환
        log.info("User Info: {}", userInfo);
        Map<String, Object> properties = (Map<String, Object>) userInfo.get("properties");

        Member newMember = new Member();
        newMember.setOauth("kakao");
        newMember.setNickName((String) properties.get("nickname"));
        newMember.setPassword((String) properties.get("nickname") + UUID.randomUUID());
        memberRepository.save(newMember);
    }

    public String getAccessToken(String code) {
        log.info("[+| Kakao Login Authorization Code: {}", code);
        log.info("authVariable: {}", authVariable);

        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("client_id", authVariable.getKakaoClientId());
        params.add("redirect_uri", REDIRECT_URL);
        params.add("client_secret", authVariable.getKakaoSecret());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(TOKEN_URL, request, Map.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> body = response.getBody();
                return body != null ? (String) body.get("access_token") : null;
            }
        } catch (Exception e) {
            log.error("Error while retrieving access token", e);
        }
        return null;
    }

    public Map<String, Object> getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    USER_INFO_URL,
                    org.springframework.http.HttpMethod.GET,
                    request,
                    Map.class
            );
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
        } catch (Exception e) {
            log.error("Error while retrieving user info", e);
        }
        return null;
    }
}
