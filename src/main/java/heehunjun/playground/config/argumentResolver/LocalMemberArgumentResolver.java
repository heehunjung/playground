package heehunjun.playground.config.argumentResolver;

import heehunjun.playground.controller.tool.token.jwt.JwtManager;
import heehunjun.playground.exception.HhjClientException;
import heehunjun.playground.exception.code.ClientErrorCode;
import heehunjun.playground.service.auth.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
public class LocalMemberArgumentResolver extends MemberArgumentResolver {

    public LocalMemberArgumentResolver(JwtManager jwtManager, AuthService authService) {
        super(jwtManager, authService);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
            throws Exception {
        String accessToken = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (accessToken == null) {
            throw new HhjClientException(ClientErrorCode.UNAUTHORIZED_MEMBER);
        }
        if (accessToken.equals("ngrinder") || accessToken.equals("test")) {
            return authService.findById(1);
        }

        String email = jwtManager.extractAccessToken(accessToken);
        return authService.getMemberByEmail(email);
    }
}
