package heehunjun.playground.controller.tool.token.googleToken;

import heehunjun.playground.dto.auth.GoogleTokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class GoogleOAuthClient {

    private static final String OAUTH_GRANT_TYPE = "authorization_code";

    private final GoogleProperties googleProperties;
    private final RestTemplate restTemplate;

    public GoogleOAuthClient(final RestTemplateBuilder restTemplateBuilder) {
        this.googleProperties = new GoogleProperties();
        this.restTemplate = restTemplateBuilder.build();
    }

    public GoogleTokenResponse getGoogleAccessToken(final String code) {
        log.info("googleProperties = {}", googleProperties);

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        final MultiValueMap<String, String> params = generateParams(code);
        final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, httpHeaders);

        final ResponseEntity<GoogleTokenResponse> googleTokenResponseResponseEntity = restTemplate
                .postForEntity(googleProperties.getTokenUri(), request, GoogleTokenResponse.class);

        return googleTokenResponseResponseEntity.getBody();
    }

    private MultiValueMap<String, String> generateParams(final String code) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", googleProperties.getClientId());
        params.add("client_secret", googleProperties.getClientSecret());
        params.add("redirect_uri", googleProperties.getRedirectUri());
        params.add("grant_type", OAUTH_GRANT_TYPE);
        return params;
    }

}
