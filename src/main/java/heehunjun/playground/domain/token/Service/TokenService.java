package heehunjun.playground.domain.token.Service;

import heehunjun.playground.domain.auth.jwt.JwtTokenProvider;
import heehunjun.playground.domain.token.domain.Token;
import heehunjun.playground.domain.token.domain.TokenRepository;
import heehunjun.playground.domain.token.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse reissueToken(final String refreshToken) {
        final String email = jwtTokenProvider.extractRefreshTokenFromAccessToken(refreshToken);
        final String newAccessToken = jwtTokenProvider.generateAccessToken(email);
        final String newRefreshToken = jwtTokenProvider.generateRefreshToken(email);
        final Token token = tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException(email));
        // todo: 커스텀 예외로 변경

        token.changeToken(newRefreshToken);

        return TokenResponse.of(newAccessToken, newRefreshToken);
    }
}
