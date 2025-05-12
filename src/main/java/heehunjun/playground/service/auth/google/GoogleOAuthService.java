package heehunjun.playground.service.auth.google;

import heehunjun.playground.controller.tool.token.googleToken.GoogleOAuthClient;
import heehunjun.playground.controller.tool.token.jwt.JwtManager;
import heehunjun.playground.domain.member.Member;
import heehunjun.playground.dto.auth.GoogleTokenResponse;
import heehunjun.playground.repository.member.MemberRepository;
import heehunjun.playground.domain.token.Token;
import heehunjun.playground.repository.token.TokenRepository;
import heehunjun.playground.dto.token.TokenResponse;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// TODO : Oauth 추상화, member 관련 로직 분리
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class GoogleOAuthService {

    private static final int PAYLOAD_INDEX = 1;
    private static final String PLATFORM = "google";

    private final JwtManager jwtManger;
    private final GoogleOAuthClient googleOauthClient;
    private final TokenRepository tokenRepository;
    private final MemberRepository memberRepository;

    public TokenResponse createToken(final String code) {
        GoogleTokenResponse googleTokenResponse = googleOauthClient.getGoogleAccessToken(code);
        final Map<String, Object> parsedPayLoad = parsePayLoad(googleTokenResponse.getIdToken());
        final String email = (String) parsedPayLoad.get("email");
        final String rawName = (String) parsedPayLoad.get("name");
        // todo: 이름 길어서 줄일수도..
        final Member member = createMemberIfNotExist(PLATFORM, email, rawName);
        final String accessToken = jwtManger.generateAccessToken(email);
        final String refreshToken = jwtManger.generateRefreshToken(email);

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
    private Member createMemberIfNotExist(String platform, String email, String nickName) {
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
