package heehunjun.playground.controller.tool.token.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtManager {

    public static final String EMAIL_KEY = "email";

    @Value("${jwt.access.secret}")
    private String jwtAccessTokenSecret;
    @Value("${jwt.access.expiration}")
    private long jwtAccessTokenExpirationInMs;
    @Value("${jwt.refresh.secret}")
    private String jwtRefreshTokenSecret;
    @Value("${jwt.refresh.expiration}")
    private long jwtRefreshTokenExpirationInMs;

    private final JwtProvider jwtProvider;
    private final JwtExtractor jwtExtractor;

    public String generateAccessToken(String email) {
        return jwtProvider.generateToken(email, jwtAccessTokenSecret, jwtAccessTokenExpirationInMs);
    }

    public String generateRefreshToken(String email) {
        return jwtProvider.generateToken(email, jwtRefreshTokenSecret, jwtRefreshTokenExpirationInMs);
    }

    public String extractAccessToken(String token) {
        return jwtExtractor.extractEmail(token, jwtAccessTokenSecret);
    }

    public String extractRefreshToken(String token) {
        return jwtExtractor.extractEmail(token, jwtRefreshTokenSecret);
    }
}
