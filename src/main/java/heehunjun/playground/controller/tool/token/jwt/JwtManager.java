package heehunjun.playground.controller.tool.token.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtManager {

    public static final String EMAIL_KEY = "email";

    public final JwtProperties jwtProperties;

    private final JwtProvider jwtProvider;
    private final JwtResolver jwtResolver;

    public String generateAccessToken(String email) {
        return jwtProvider.generateToken(email, jwtProperties.getAccessTokenSecret(), jwtProperties.getAccessTokenExpiration());
    }

    public String generateRefreshToken(String email) {
        return jwtProvider.generateToken(email, jwtProperties.getRefreshTokenSecret(), jwtProperties.getRefreshTokenExpiration());
    }

    public String extractAccessToken(String token) {
        return jwtResolver.extractEmail(token, jwtProperties.getAccessTokenSecret());
    }

    public String extractRefreshToken(String token) {
        return jwtResolver.extractEmail(token, jwtProperties.getRefreshTokenSecret());
    }
}
