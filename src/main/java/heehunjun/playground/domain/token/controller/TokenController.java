package heehunjun.playground.domain.token.controller;

import heehunjun.playground.domain.auth.jwt.JwtTokenExtractor;
import heehunjun.playground.domain.token.Service.TokenService;
import heehunjun.playground.domain.token.dto.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/token")
public class TokenController {

    private static final String ACCESS_TOKEN_HEADER = HttpHeaders.AUTHORIZATION;
    private static final String REFRESH_TOKEN_HEADER = "Authorization-Refresh";

    private final JwtTokenExtractor jwtTokenExtractor;
    private TokenService tokenService;

    @PostMapping("/reissue")
    public ResponseEntity<Void> reissueAccessToken(final HttpServletRequest request, final HttpServletResponse response) {
        String refreshToken = jwtTokenExtractor.extractRefreshToken(request);
        TokenResponse tokenResponse = tokenService.reissueToken(refreshToken);
        response.setHeader(ACCESS_TOKEN_HEADER, tokenResponse.getAccessToken());
        response.setHeader(REFRESH_TOKEN_HEADER, tokenResponse.getRefreshToken());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(final HttpServletRequest request, final HttpServletResponse response) {
        String refreshToken = jwtTokenExtractor.extractRefreshToken(request);
        if (refreshToken == null) {
            throw new RuntimeException("Refresh token is null");
        }
        tokenService.deleteToken(refreshToken);
        return ResponseEntity.ok().build();
    }
}
