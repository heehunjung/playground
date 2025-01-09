package heehunjun.playground.domain.auth.interceptor;

import heehunjun.playground.domain.auth.jwt.JwtTokenExtractor;
import heehunjun.playground.domain.auth.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class MyInterceptor implements HandlerInterceptor {

    private final JwtTokenExtractor jwtTokenExtractor;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // Option 요청 처리해야할 수도 있다.
        final String accessToken = jwtTokenExtractor.extractAccessToken(request);
        final String email = jwtTokenProvider.extractEmailFromAccessToken(accessToken);
        log.info("Extracted email: {}", email);
        return true;
    }
}
