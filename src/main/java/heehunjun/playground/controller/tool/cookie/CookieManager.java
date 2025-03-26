package heehunjun.playground.controller.tool.cookie;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CookieManager {

    private static final String ACCESS_TOKEN_HEADER = HttpHeaders.AUTHORIZATION;
    private static final String REFRESH_TOKEN_HEADER = "Authorization-Refresh";

    @Value("${jwt.refresh.expiration}")
    private long refreshExpiration;
    @Value("${jwt.access.expiration}")
    private long accessExpiration;

    private final CookieExtractor cookieExtractor;
    private final CookieProvider cookieProvider;

    public ResponseCookie createAccessCookie(final String token) {
        return cookieProvider.generateCookie(ACCESS_TOKEN_HEADER, token, accessExpiration);
    }

    public ResponseCookie generateRefreshToken(final String token) {
        return cookieProvider.generateCookie(REFRESH_TOKEN_HEADER, token, refreshExpiration);
    }

    public ResponseCookie generateExpireToken() {
        return cookieProvider.generateCookie(REFRESH_TOKEN_HEADER, "", 0);
    }

    public String extractAccessToken(final HttpServletRequest request) {
        return cookieExtractor.extract(request, ACCESS_TOKEN_HEADER);
    }

    public String extractRefreshToken(final HttpServletRequest request) {
        return cookieExtractor.extract(request, REFRESH_TOKEN_HEADER);
    }
}
