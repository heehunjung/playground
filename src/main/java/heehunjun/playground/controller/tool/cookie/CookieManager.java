package heehunjun.playground.controller.tool.cookie;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CookieManager {

    public final String ACCESS_TOKEN_HEADER = HttpHeaders.AUTHORIZATION;
    public final String REFRESH_TOKEN_HEADER = "Authorization-Refresh";
    private final CookieProvider cookieProvider;

    @Value("${jwt.refresh.expiration}")
    private long refreshExpiration;
    @Value("${jwt.access.expiration}")
    private long accessExpiration;

    public ResponseCookie createAccessCookie(String token) {
        return cookieProvider.generateCookie(ACCESS_TOKEN_HEADER, token, accessExpiration);
    }

    public ResponseCookie generateRefreshToken(String token) {
        return cookieProvider.generateCookie(REFRESH_TOKEN_HEADER, token, refreshExpiration);
    }

    public ResponseCookie generateExpireToken() {
        return cookieProvider.generateCookie(REFRESH_TOKEN_HEADER, "", 0);
    }
}
