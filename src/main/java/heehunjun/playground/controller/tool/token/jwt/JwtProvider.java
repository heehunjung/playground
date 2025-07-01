package heehunjun.playground.controller.tool.token.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    public String generateToken(String email, String secret, long tokenExpirationTime) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + tokenExpirationTime);
        SecretKey secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                SignatureAlgorithm.HS256.getJcaName());

        return Jwts.builder()
                .claim(JwtManager.EMAIL_KEY, email)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(secretKey)
                .compact();
    }
}


