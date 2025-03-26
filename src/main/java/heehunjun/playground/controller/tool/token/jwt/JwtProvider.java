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

    public String generateToken(final String email, final String secret, final long tokenExpirationTime) {
        final Date now = new Date();
        final Date expireDate = new Date(now.getTime() + tokenExpirationTime);
        final SecretKey secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                SignatureAlgorithm.HS256.getJcaName());

        return Jwts.builder()
                .claim(JwtManager.EMAIL_KEY, email)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(secretKey)
                .compact();
    }
}

// 정리해보면 초기 로그인 시 엑세스토큰 생성, 리프레쉬 토큰 생성한다 !
// 리프레쉬 토큰은 서버에 저장할거야
// 요청은 엑세스 토큰으로만 받아
// 고민하는 점 엑세스 토큰, 리프레시 토큰 둘다 보내야되나 ?
