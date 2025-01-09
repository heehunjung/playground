package heehunjun.playground.domain.auth.oauth.service.kakao;

import heehunjun.playground.domain.auth.jwt.JwtTokenProvider;
import heehunjun.playground.domain.auth.oauth.client.KakaoOAuthClient;
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
public class KakaoOAuthService {

    private static final String TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";
    private static final String REDIRECT_URL = "http://localhost:8080/oauth2/kakao";
    private static final String KAKAO = "kakao";

    private final KakaoProperties kakaoProperties;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;
    private final KakaoOAuthClient kakaoOAuthClient;

    // todo : 중복 로그인 ? ? ?
    public TokenResponse createToken(final String code) {
        KakaoTokenResponse kakaoTokenResponse = kakaoOAuthClient.getKakaoAccessToken(code);
        String kakaoAccessToken = kakaoTokenResponse.getAccessToken();

        Map<String, Object> userInfo = kakaoOAuthClient.getUserInfo(kakaoAccessToken);
        Map<String, Object> properties = (Map<String, Object>) userInfo.get("properties");
        final String nickname = (String) properties.get("nickname");
        final String email = (String) properties.get("email");
        final Member member = createMemberIfNotExist(KAKAO, nickname, email);

        final String accessToken = jwtTokenProvider.generateAccessToken(member.getEmail());
        final String refreshToken = jwtTokenProvider.generateRefreshToken(member.getEmail());

        saveOrUpdateRefreshToken(member, refreshToken);
        return TokenResponse.of(accessToken, refreshToken);
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
