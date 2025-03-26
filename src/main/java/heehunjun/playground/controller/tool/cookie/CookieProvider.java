package heehunjun.playground.controller.tool.cookie;

import java.time.Duration;
import org.springframework.boot.web.server.Cookie.SameSite;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieProvider {

    private static final String PATH = "/";
    private static final String SAME_SITE = SameSite.NONE.attributeValue();

    public ResponseCookie generateCookie(String key, String value, long maxAge) {
        return ResponseCookie.from(key, value)
                .path(PATH)
                .sameSite(SAME_SITE)
                .httpOnly(true)
                .maxAge(Duration.ofMillis(maxAge))
                .build();
    }
}
