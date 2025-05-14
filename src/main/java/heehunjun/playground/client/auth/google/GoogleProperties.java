package heehunjun.playground.client.auth.google;

import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConfigurationProperties(prefix = "oauth.google")
@Getter
@Setter
public class GoogleProperties {

    private String clientId;
    private String clientSecret;
    private String endPoint;
    private String responseType;
    private List<String> scopes;
    private String accessType;
    private String tokenUri;
    private String redirectUri;

    @PostConstruct
    public void validate() {
        if (tokenUri == null || tokenUri.isBlank()) {
            log.error("Google tokenUri is not configured");
            throw new IllegalStateException("Google tokenUri is not configured");
        }
        if (!tokenUri.startsWith("http://") && !tokenUri.startsWith("https://")) {
            log.error("Google tokenUri has invalid scheme: {}", tokenUri);
            throw new IllegalStateException("Google tokenUri has invalid scheme: " + tokenUri);
        }
        log.info("Google Properties loaded: tokenUri={}, redirectUri={}, clientId={}",
                tokenUri, redirectUri, clientId);
    }
}

