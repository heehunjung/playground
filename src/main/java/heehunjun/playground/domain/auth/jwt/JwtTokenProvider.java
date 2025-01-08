package heehunjun.playground.domain.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private static final String EXPIRED_ACCESS_TOKEN_MESSAGE = "EXPIRED_ACCESS_TOKEN";
    private static final String EXPIRED_REFRESH_TOKEN_MESSAGE = "EXPIRED_REFRESH_TOKEN";
    private static final String EMAIL_KEY = "email";

    @Value("${jwt.access.secret}")
    private String jwtAccessTokenSecret;
    @Value("${jwt.access.expiration}")
    private long jwtAccessTokenExpirationInMs;

    @Value("${jwt.refresh.secret}")
    private String jwtRefreshTokenSecret;
    @Value("${jwt.refresh.expiration}")
    private long jwtRefreshTokenExpirationInMs;

    // access
    public String generateAccessToken(final String email) {
        final Date now = new Date();
        final Date expireDate = new Date(now.getTime() + jwtAccessTokenExpirationInMs);
        final SecretKey secretKey = new SecretKeySpec(jwtAccessTokenSecret.getBytes(StandardCharsets.UTF_8),
                SignatureAlgorithm.HS256.getJcaName());

        return Jwts.builder()
                .claim(EMAIL_KEY, email)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .compact();
    }

    public String extractEmailFromAccessToken(final String token) {
        validationAccessToken(token);
        final Jws<Claims> claimsJws = getAccessTokenParser().parseClaimsJws(token);
        String extractEmail = claimsJws.getBody().get(EMAIL_KEY, String.class);

        if (extractEmail == null) {
            final String logMessage = "[인증 실패] JWT 엑세스 토큰 Payload 이메일 누락 - 토큰 :" + token;

            throw new RuntimeException(logMessage);
            // todo: 커스텀 예외로 변경
        }
        return extractEmail;
    }

    private void validationAccessToken(String token) {
        try {
            final Claims claims = getAccessTokenParser().parseClaimsJws(token).getBody();
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            final String logMessage = "[인증 실패] 잘못된 액세스 토큰 - 토큰 : " + token;

            throw new RuntimeException(logMessage);
            //todo : 커스텀 예외로 변경
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(null, null, EXPIRED_ACCESS_TOKEN_MESSAGE);
        }
    }

    private JwtParser getAccessTokenParser() {
        return Jwts.parserBuilder()
                .setSigningKey(jwtAccessTokenSecret.getBytes((StandardCharsets.UTF_8)))
                .build();
    }

    // refresh
    public String generateRefreshToken(final String email) {
        final Date now = new Date();
        final Date expireDate = new Date(now.getTime() + jwtRefreshTokenExpirationInMs);
        final SecretKey secretKey = new SecretKeySpec(jwtRefreshTokenSecret.getBytes(StandardCharsets.UTF_8),
                SignatureAlgorithm.HS256.getJcaName());

        return Jwts.builder()
                .claim(EMAIL_KEY, email)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(secretKey)
                .compact();
    }

    public String extractRefreshTokenFromAccessToken(final String token) {
        validateRefreshToken(token);
        final Jws<Claims> claimsJws = getRefreshTokenParser().parseClaimsJws(token);
        String extractedEmail = claimsJws.getBody().get(EMAIL_KEY, String.class);
        if (extractedEmail == null) {
            final String logMessage = "인증 실패(JWT 리프레시 토큰 Payload 이메일 누락) - 토큰 : " + token;

            throw new RuntimeException(logMessage);
            // todo: 커스텀 언체크드 예외로 변경 !
        }
        return extractedEmail;
    }

    private void validateRefreshToken(String token) {
        try {
            final Claims claims = getAccessTokenParser().parseClaimsJws(token).getBody();
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            final String logMessage = "[인증 실패] 잘못된 리프레쉬 토큰 - 토큰 : " + token;

            throw new RuntimeException(logMessage);
            //todo : 커스텀 예외로 변경
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(null, null, EXPIRED_REFRESH_TOKEN_MESSAGE);
        }
    }

    private JwtParser getRefreshTokenParser() {
        return Jwts.parserBuilder()
                .setSigningKey(jwtRefreshTokenSecret.getBytes(StandardCharsets.UTF_8))
                .build();
    }
}
