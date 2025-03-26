package heehunjun.playground.service.auth.kakao;

import heehunjun.playground.controller.tool.token.jwt.JwtManager;
import heehunjun.playground.controller.tool.token.kakaoToken.KakaoOAuthClient;
import heehunjun.playground.domain.member.Member;
import heehunjun.playground.dto.auth.KakaoTokenResponse;
import heehunjun.playground.repository.member.MemberRepository;
import heehunjun.playground.domain.token.Token;
import heehunjun.playground.repository.token.TokenRepository;
import heehunjun.playground.dto.token.TokenResponse;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class KakaoOAuthService {

    private static final String KAKAO = "kakao";

    private final MemberRepository memberRepository;
    private final JwtManager jwtManager;
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

        final String accessToken = jwtManager.generateAccessToken(member.getEmail());
        final String refreshToken = jwtManager.generateRefreshToken(member.getEmail());

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
