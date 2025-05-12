package heehunjun.playground.controller.token;

import heehunjun.playground.controller.tool.cookie.CookieManager;
import heehunjun.playground.controller.tool.token.jwt.JwtManager;
import heehunjun.playground.domain.token.Token;
import heehunjun.playground.service.token.TokenService;
import heehunjun.playground.dto.token.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/token")
public class TokenController {

    private static final String REFRESH_TOKEN = "refresh token";

    private final CookieManager cookieManager;
    private final JwtManager jwtManger;
    private final TokenService tokenService;

    @PostMapping("/reissue")
    public ResponseEntity<Void> reissueAccessToken(@CookieValue(REFRESH_TOKEN) String refreshToken) {
        Token token = tokenService.getToken(refreshToken);
        TokenResponse tokenResponse = getTokenResponse(refreshToken, token);

        ResponseCookie accessTokenCookie = cookieManager.createAccessCookie(
                tokenResponse.getAccessToken());
        ResponseCookie refreshTokenCookie = cookieManager.generateRefreshToken(
                tokenResponse.getRefreshToken());

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .build();
    }

    private TokenResponse getTokenResponse(String refreshToken, Token token) {
        String email = jwtManger.extractRefreshToken(refreshToken);
        String newAccessToken = jwtManger.generateAccessToken(email);
        String newRefreshToken = jwtManger.generateRefreshToken(email);
        token.changeToken(newRefreshToken);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(REFRESH_TOKEN) String refreshToken) {
        tokenService.deleteToken(refreshToken);
        ResponseCookie expireToken = cookieManager.generateExpireToken();

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, expireToken.toString())
                .build();
    }
}
