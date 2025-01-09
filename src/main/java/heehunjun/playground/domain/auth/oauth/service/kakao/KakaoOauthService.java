package heehunjun.playground.domain.auth.oauth.service.kakao;

import heehunjun.playground.domain.auth.jwt.JwtTokenProvider;
import heehunjun.playground.domain.auth.oauth.config.KakaoProperties;
import heehunjun.playground.domain.member.domain.Member;
import heehunjun.playground.domain.member.domain.MemberRepository;
import heehunjun.playground.domain.token.domain.Token;
import heehunjun.playground.domain.token.domain.TokenRepository;
import heehunjun.playground.domain.token.dto.TokenResponse;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class KakaoOauthService {

    private static final String TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";
    private static final String REDIRECT_URL = "http://localhost:8080/oauth2/kakao";
    private static final String KAKAO = "kakao";

    private final KakaoProperties kakaoProperties;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;

    // todo : 중복 로그인 ? ? ?
    public TokenResponse createToken(Map<String, Object> userInfo) {
        Map<String, Object> properties = (Map<String, Object>) userInfo.get("properties");

        final String nickname = (String) properties.get("nickname");
        final String email = (String) properties.get("email");
        final Member member = createMemberIfNotExist(KAKAO, nickname, email);

        final String accessToken = jwtTokenProvider.generateAccessToken(member.getEmail());
        final String refreshToken = jwtTokenProvider.generateRefreshToken(member.getEmail());

        saveOrUpdateRefreshToken(member, refreshToken);

        return TokenResponse.of(accessToken, refreshToken);
    }

    public String getKakaoAccessToken(String code) {
        log.info("[+| Kakao Login Authorization Code: {}", code);
        log.info("authVariable: {}", kakaoProperties);

        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("client_id", kakaoProperties.getKakaoClientId());
        params.add("redirect_uri", REDIRECT_URL);
        params.add("client_secret", kakaoProperties.getKakaoSecret());

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

    private Member createMemberIfNotExist(String platform, String email, String nickName) {
        final Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isPresent()) {
            return optionalMember.get();
        }

        final Member member = new Member(nickName, email, platform);
        return memberRepository.save(member);
    }

    private void saveOrUpdateRefreshToken(Member member, String refreshToken) {
        tokenRepository.findByMember(member)
                .ifPresentOrElse(
                        token -> token.changeToken(refreshToken),
                        () -> tokenRepository.save(new Token(member, refreshToken))
                );
    }
}
