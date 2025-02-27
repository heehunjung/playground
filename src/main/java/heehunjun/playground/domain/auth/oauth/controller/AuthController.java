package heehunjun.playground.domain.auth.oauth.controller;

import static java.lang.System.getenv;

import heehunjun.playground.domain.auth.oauth.service.google.GoogleOAuthService;
import heehunjun.playground.domain.auth.oauth.service.kakao.KakaoOAuthService;
import heehunjun.playground.domain.token.domain.Token;
import heehunjun.playground.domain.token.dto.TokenResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth2")
public class AuthController {
    private static final String QUERY_START_MARK = "?";
    private static final String QUERY_AND_MARK = "&";
    private static final String QUERY_PARAM_ACCESS_TOKEN_KEY = "accessToken=";
    private static final String QUERY_PARAM_REFRESH_TOKEN_KEY = "refreshToken=";
    private static final String BASE_URL = "/login";
    @Value("${base-domain.front}")
    private String baseDomain;

    private final KakaoOAuthService kakaoOauthService;
    private final GoogleOAuthService googleOauthService;
    // todo: 추상화 방법
    @GetMapping("/kakao")
    public void oauth2Kakao(@RequestParam String code, HttpServletResponse response) throws IOException {
        final TokenResponse tokens = kakaoOauthService.createToken(code);
        final String accessToken = tokens.getAccessToken();
        final String refreshToken = tokens.getRefreshToken();

        String url = generateUrl(BASE_URL, accessToken, refreshToken);
        response.sendRedirect(url);
    }

    @GetMapping("/google")
    public void oauth2Google(final HttpServletResponse response, @RequestParam final String code) throws IOException {
        final TokenResponse tokens = googleOauthService.createToken(code);
        final String accessToken = tokens.getAccessToken();
        final String refreshToken = tokens.getRefreshToken();

        String url = generateUrl(BASE_URL, accessToken, refreshToken);
        response.sendRedirect(url);
    }

    private String generateUrl(final String baseUrl, final String accessToken, final String refreshToken) {
        StringBuilder sb = new StringBuilder();
        StringBuilder url = sb.append(baseDomain)
                .append(baseUrl)
                .append(QUERY_START_MARK)
                .append(QUERY_PARAM_ACCESS_TOKEN_KEY)
                .append(accessToken)
                .append(QUERY_AND_MARK)
                .append(QUERY_PARAM_REFRESH_TOKEN_KEY)
                .append(refreshToken);
        return url.toString();
    }
}