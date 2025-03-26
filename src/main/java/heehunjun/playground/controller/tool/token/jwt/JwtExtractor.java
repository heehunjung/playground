package heehunjun.playground.controller.tool.token.jwt;

import heehunjun.playground.exception.hhjClientError;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class JwtExtractor {

    private static final String PREFIX = "Bearer ";


    public String extractEmail(final String token, final String secret) {
        final Jws<Claims> claimsJws =  validateToken(token,secret);

        return claimsJws.getBody().get(JwtManager.EMAIL_KEY, String.class);
    }

    private Jws<Claims> validateToken(String token, String secret) {
        try {
            return getTokenParser(secret).parseClaimsJws(token);
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            throw new hhjClientError("[인증 실패] 잘못된 리프레쉬 토큰 - 토큰 : " + token, HttpStatus.UNAUTHORIZED);
        } catch (ExpiredJwtException e) {
            throw new hhjClientError("로그인 유효시간이 만료되었습니다.", HttpStatus.UNAUTHORIZED);
        }
    }

    private JwtParser getTokenParser(final String secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret.getBytes((StandardCharsets.UTF_8)))
                .build();
    }
}
