package heehunjun.playground.domain.auth.oauth.service.google;

import heehunjun.playground.domain.auth.jwt.JwtTokenProvider;
import heehunjun.playground.domain.auth.oauth.client.GoogleOauthClient;
import heehunjun.playground.domain.auth.oauth.config.GoogleProperties;
import heehunjun.playground.domain.member.domain.Member;
import heehunjun.playground.domain.member.domain.MemberRepository;
import heehunjun.playground.domain.token.domain.Token;
import heehunjun.playground.domain.token.domain.TokenRepository;
import heehunjun.playground.domain.token.dto.TokenResponse;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class GoogleOauthService {

    private static final int PAYLOAD_INDEX = 1;
    private static final String PLATFORM = "google";

    private final JwtTokenProvider jwtTokenProvider;
    private final GoogleOauthClient googleOauthClient;
    private final TokenRepository tokenRepository;
    private final MemberRepository memberRepository;

    public TokenResponse createToken(final String code) {
        GoogleTokenResponse googleTokenResponse = googleOauthClient.getGoogleAccessToken(code);
        final Map<String, Object> parsedPayLoad = parsePayLoad(googleTokenResponse.getIdToken());
        final String email = (String) parsedPayLoad.get("email");
        final String rawName = (String) parsedPayLoad.get("name");
        // todo: 이름 길어서 줄일수도..
        final Member member = createMemberIfNotExist(PLATFORM, email, rawName);

        final String accessToken = jwtTokenProvider.generateAccessToken(email);
        final String refreshToken = jwtTokenProvider.generateRefreshToken(email);

        saveOrUpdateRefreshToken(member, refreshToken);

        log.info("토큰 생성 - 사용자 이메일 : {}", email);
        return new TokenResponse(accessToken, refreshToken);
    }

    private void saveOrUpdateRefreshToken(Member member, String refreshToken) {
        tokenRepository.findByMember(member)
                .ifPresentOrElse(
                        token -> token.changeToken(refreshToken),
                        () -> tokenRepository.save(new Token(member, refreshToken))
                );
    }

    //todo : 중복되는 코드네 추상 클래스 ? 인터페이스 ?
    private Member createMemberIfNotExist(String platform, String email,  String nickName) {
        final Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isPresent()) {
            return optionalMember.get();
        }

        final Member member = new Member(nickName, email, platform);
        return memberRepository.save(member);
    }

    private Map<String, Object> parsePayLoad(final String googleIdToken) {
        final String payLoad = googleIdToken.split("\\.")[PAYLOAD_INDEX];
        final String decodedPayLoad = new String(Base64.getUrlDecoder().decode(payLoad));
        final JacksonJsonParser jacksonJsonParser = new JacksonJsonParser();
        return jacksonJsonParser.parseMap(decodedPayLoad);
    }
}
