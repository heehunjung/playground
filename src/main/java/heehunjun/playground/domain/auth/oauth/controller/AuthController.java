package heehunjun.playground.domain.auth.oauth.controller;

import static java.lang.System.getenv;

import heehunjun.playground.domain.auth.oauth.service.KakaoOauthService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth2")
public class AuthController {
    private static final String TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";
    private static final String REDIRECT_URL = "http://localhost:8080/oauth2/kakao";

    private final KakaoOauthService kakaoOauthService;

    @GetMapping("/kakao")
    public ResponseEntity<String> oauth2Kakao(@RequestParam String code) {
        // 1. Access Token 요청
        String accessToken = kakaoOauthService.getKakaoAccessToken(code);
        if (accessToken == null) {
            return ResponseEntity.badRequest().body("Failed to retrieve access token");
        }

        // 2. 사용자 정보 요청
        Map<String, Object> userInfo = kakaoOauthService.getUserInfo(accessToken);
        if (userInfo == null) {
            return ResponseEntity.badRequest().body("Failed to retrieve user info");
        }

        kakaoOauthService.saveNewMember(userInfo);
        return ResponseEntity.ok("User Info: " + userInfo);
    }
    // todo: 더 줄일 수 있나?
}