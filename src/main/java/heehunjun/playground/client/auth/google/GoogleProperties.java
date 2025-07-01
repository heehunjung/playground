package heehunjun.playground.client.auth.google;

import java.util.List;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "oauth.google")
public class GoogleProperties {

    private final String clientId;
    private final String clientSecret;
    private final String endPoint;
    private final String responseType;
    private final List<String> scopes;
    private final String accessType;
    private final String tokenUri;
    private final String redirectUri;

    public GoogleProperties(String clientId, String clientSecret, String endPoint,
                            String responseType, List<String> scopes, String accessType,
                            String tokenUri, String redirectUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.endPoint = endPoint;
        this.responseType = responseType;
        this.scopes = scopes;
        this.accessType = accessType;
        this.tokenUri = tokenUri;
        this.redirectUri = redirectUri;
    }
}

