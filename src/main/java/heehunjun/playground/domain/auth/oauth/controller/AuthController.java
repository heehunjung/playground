package heehunjun.playground.domain.auth.oauth.controller;

import static java.lang.System.getenv;

import heehunjun.playground.domain.auth.oauth.service.google.GoogleOauthService;
import heehunjun.playground.domain.auth.oauth.service.kakao.KakaoOauthService;
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

    @Value("${base-domain.front}")
    private String baseDomain;

    private final KakaoOauthService kakaoOauthService;
    private final GoogleOauthService googleOauthService;

    @GetMapping("/kakao")
    public void oauth2Kakao(@RequestParam String code, HttpServletResponse response) throws IOException {
        // 1. Access Token 요청
        String accessToken = kakaoOauthService.getKakaoAccessToken(code);
        if (accessToken == null) {
            throw new RuntimeException("Access token is null");
        }

        // 2. 사용자 정보 요청
        Map<String, Object> userInfo = kakaoOauthService.getUserInfo(accessToken);
        if (userInfo == null) {
            throw new RuntimeException("UserInfo is null");
        }

        final String baseLine = "/login";
        TokenResponse token = kakaoOauthService.createToken(userInfo);
        String url = generateUrl(baseLine, token.getAccessToken(), token.getRefreshToken());

        response.sendRedirect(url);
    }

    @GetMapping("/google")
    public void oauth2Google(final HttpServletResponse response, @RequestParam final String code) throws IOException {
        log.info("여기");
        final String baseUrl = "/login";
        final TokenResponse tokens = googleOauthService.createToken(code);
        final String accessToken = tokens.getAccessToken();
        final String refreshToken = tokens.getRefreshToken();

        String url = generateUrl(baseUrl, accessToken, refreshToken);
        log.info("url = {}", url);
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