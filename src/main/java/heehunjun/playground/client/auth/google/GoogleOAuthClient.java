package heehunjun.playground.client.auth.google;

import heehunjun.playground.client.auth.OAuthClient;
import heehunjun.playground.dto.member.MemberInfo;
import heehunjun.playground.dto.member.OauthToken;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Slf4j
@Component("google")
public class GoogleOAuthClient implements OAuthClient {

    private static final String OAUTH_GRANT_TYPE = "authorization_code";
    private static final int PAYLOAD_INDEX = 1;
    public static final String REGEX = "\\.";

    private final GoogleProperties googleProperties;
    private final RestClient restClient;

    public GoogleOAuthClient(RestClient.Builder restClientBuilder, GoogleProperties googleProperties) {
        this.restClient = restClientBuilder.build();
        this.googleProperties = googleProperties;
    }

    @Override
    public OauthToken getOauthToken(String code) {
        MultiValueMap<String, String> params = generateParams(code);

        return restClient.post()
                .uri(googleProperties.getTokenUri())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(params)
                .retrieve()
                .body(OauthToken.class);
    }

    private MultiValueMap<String, String> generateParams(String code) {
        String decodedVerificationCode = URLDecoder.decode(code, StandardCharsets.UTF_8);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", decodedVerificationCode);
        params.add("client_id", googleProperties.getClientId());
        params.add("client_secret", googleProperties.getClientSecret());
        params.add("redirect_uri", googleProperties.getRedirectUri());
        params.add("grant_type", OAUTH_GRANT_TYPE);
        return params;
    }

    @Override
    public MemberInfo getMemberInfo(String token) {
        String payLoad = token.split(REGEX)[PAYLOAD_INDEX];
        String decodedPayLoad = new String(Base64.getUrlDecoder().decode(payLoad)); // <- !!!!!!!!!!!!!!!!!!!!!!!!
        JacksonJsonParser jacksonJsonParser = new JacksonJsonParser();
        Map<String, Object> parsedPayLoad = jacksonJsonParser.parseMap(decodedPayLoad);

        return new MemberInfo((String) parsedPayLoad.get("email"), (String) parsedPayLoad.get("name"));
    }
}
