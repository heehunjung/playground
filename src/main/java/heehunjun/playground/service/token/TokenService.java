package heehunjun.playground.service.token;

import heehunjun.playground.domain.token.Token;
import heehunjun.playground.exception.hhjClientException;
import heehunjun.playground.repository.token.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    public Token getToken(final String refreshToken) {
        return tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new hhjClientException("로그인 정보가 없습니다.", HttpStatus.UNAUTHORIZED));
    }

    public void deleteToken(final String refreshToken) {
        final Token token = tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new hhjClientException("로그인 정보가 없습니다.", HttpStatus.UNAUTHORIZED));
        tokenRepository.delete(token);
    }
}
