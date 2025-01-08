package heehunjun.playground.domain.auth.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtTokenExtractor {

    private static final String PREFIX = "Bearer ";
    private static final String ACCESS_TOKEN_HEADER = HttpHeaders.AUTHORIZATION;
    private static final String REFRESH_TOKEN_HEADER = "Authorization-Refresh";

    public String extractAccessToken(final HttpServletRequest request) {
        final String accessToken = request.getHeader(ACCESS_TOKEN_HEADER);
        if (StringUtils.hasText(accessToken) && accessToken.startsWith(PREFIX)) {
            return accessToken.substring(PREFIX.length());
        }
        final String logMessage = "[인증 실패]엑세스 토큰 추출 실패 - 토큰 : " + accessToken;
        throw new RuntimeException(logMessage);
        //todo: 커스텀 언체크드 예외 만들어줘야 한다.
    }

    public String extractRefreshToken(final HttpServletRequest request) {
        final String refreshToken = request.getHeader(REFRESH_TOKEN_HEADER);
        if (StringUtils.hasText(refreshToken) && refreshToken.startsWith(PREFIX)) {
            return refreshToken.substring(PREFIX.length());
        }
        final String logMessage = "[인증 실패]리프레쉬 토큰 추출 실패 - 토큰 : " + refreshToken;
        throw new RuntimeException(logMessage);
        //todo: 커스텀 언체크드 예외 만들어줘야 한다.
    }
}
