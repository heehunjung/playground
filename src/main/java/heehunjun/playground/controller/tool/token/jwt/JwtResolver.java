package heehunjun.playground.controller.tool.token.jwt;

import heehunjun.playground.exception.code.ClientErrorCode;
import heehunjun.playground.exception.hhjClientException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class JwtResolver {

    private static final String PREFIX = "Bearer ";

    public String extractEmail(final String token, final String secret) {
        final Jws<Claims> claimsJws = validateToken(token, secret);

        return claimsJws.getBody().get(JwtManager.EMAIL_KEY, String.class);
    }

    private Jws<Claims> validateToken(String token, String secret) {
        try {
            return getTokenParser(secret).parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new hhjClientException(ClientErrorCode.EXPIRED_TOKEN);
        } catch (JwtException e) {
            throw new hhjClientException(ClientErrorCode.UNAUTHORIZED_MEMBER);
        }
    }

    private JwtParser getTokenParser(final String secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret.getBytes((StandardCharsets.UTF_8)))
                .build();
    }
}
