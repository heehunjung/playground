package heehunjun.playground.interceptor;

import heehunjun.playground.controller.tool.token.jwt.JwtManager;
import heehunjun.playground.exception.code.ClientErrorCode;
import heehunjun.playground.exception.HhjClientException;
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
        if(request.getCookies() == null || request.getCookies().length == 0) {
            throw new HhjClientException(ClientErrorCode.UNAUTHORIZED_MEMBER);
        }

        String accessToken = String.valueOf(Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(ACCESS_TOKEN))
                .map(Cookie::getValue)
                .findFirst());

        if (accessToken == null) {
            throw new HhjClientException(ClientErrorCode.UNAUTHORIZED_MEMBER);
        }
        String email = jwtManager.extractAccessToken(accessToken);

        if (email == null) {
            throw new HhjClientException(ClientErrorCode.UNAUTHORIZED_MEMBER);
        }
        return true;
    }
}
