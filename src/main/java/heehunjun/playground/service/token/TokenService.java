package heehunjun.playground.service.token;

import heehunjun.playground.domain.token.Token;
import heehunjun.playground.exception.HhjClientException;
import heehunjun.playground.exception.code.ClientErrorCode;
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

    public Token getToken(String refreshToken) {
        return tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new HhjClientException(ClientErrorCode.UNAUTHORIZED_MEMBER));
    }

    public void deleteToken(String refreshToken) {
        Token token = tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new HhjClientException(ClientErrorCode.UNAUTHORIZED_MEMBER));
        tokenRepository.delete(token);
    }
}
