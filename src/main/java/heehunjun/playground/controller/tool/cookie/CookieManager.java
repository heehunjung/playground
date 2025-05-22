package heehunjun.playground.controller.tool.cookie;

import heehunjun.playground.controller.tool.token.jwt.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CookieManager {

    public final static String REFRESH_TOKEN_HEADER = "Authorization-Refresh";

    private final CookieProvider cookieProvider;
    private final JwtProperties jwtProperties;

    public ResponseCookie generateRefreshToken(String token) {
        return cookieProvider.generateCookie(REFRESH_TOKEN_HEADER, token, jwtProperties.getRefreshTokenExpiration());
    }

    public ResponseCookie generateExpireToken() {
        return cookieProvider.generateCookie(REFRESH_TOKEN_HEADER, "", 0);
    }
}
