package heehunjun.playground.client.auth.google;

import java.util.List;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class GoogleProperties {

    @Value("${oauth.google.client-id}")
    private String clientId;

    @Value("${oauth.google.client-secret}")
    private String clientSecret;

    @Value("${oauth.google.end-point}")
    private String endPoint;

    @Value("${oauth.google.response-type}")
    private String responseType;

    @Value("${oauth.google.scopes}")
    private List<String> scopes;

    @Value("${oauth.google.access-type}")
    private String accessType;

    @Value("${oauth.google.token-uri}")
    private String tokenUri;

    @Value("${oauth.google.redirect-uri}")
    private String redirectUri;
}
