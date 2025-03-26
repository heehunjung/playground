package heehunjun.playground.interceptor;

import heehunjun.playground.controller.tool.cookie.CookieManager;
import heehunjun.playground.controller.tool.token.jwt.JwtManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final CookieManager cookieManager;
    private final JwtManager jwtManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        final String accessToken = cookieManager.extractAccessToken(request);
        final String email = jwtManager.extractAccessToken(accessToken);
        log.info("Extracted email: {}", email);

        if (email == null) {
            response.setStatus(404);
            return false;
        }
        return true;
    }
}
