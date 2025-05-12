package heehunjun.playground.interceptor;

import heehunjun.playground.controller.tool.cookie.CookieManager;
import heehunjun.playground.controller.tool.token.jwt.JwtManager;
import heehunjun.playground.exception.code.ClientErrorCode;
import heehunjun.playground.exception.hhjClientException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private static final String ACCESS_TOKEN = "accessToken";

    private final JwtManager jwtManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) {
        String accessToken = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(ACCESS_TOKEN))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new hhjClientException(ClientErrorCode.UNAUTHORIZED_MEMBER));
        String email = jwtManager.extractAccessToken(accessToken);
        log.info("Extracted email: {}", email);

        if (email == null) {
            response.setStatus(404);
            return false;
        }
        return true;
    }
}
