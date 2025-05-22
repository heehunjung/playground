package heehunjun.playground.controller.tool.token.jwt;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private final Access access;
    private final Refresh refresh;

    public JwtProperties(Access access, Refresh refresh) {
        this.access = access;
        this.refresh = refresh;
    }

    @Getter
    public static class Access {
        private String secret;
        private long expiration;

        public Access(String secret, long expiration) {
            this.secret = secret;
            this.expiration = expiration;
        }
    }

    @Getter
    public static class Refresh {
        private String secret;
        private long expiration;

        public Refresh(String secret, long expiration) {
            this.secret = secret;
            this.expiration = expiration;
        }
    }

    public String getAccessTokenSecret() {
        return access.getSecret();
    }

    public long getAccessTokenExpiration() {
        return access.getExpiration();
    }

    public String getRefreshTokenSecret() {
        return refresh.getSecret();
    }

    public long getRefreshTokenExpiration() {
        return refresh.getExpiration();
    }
}

