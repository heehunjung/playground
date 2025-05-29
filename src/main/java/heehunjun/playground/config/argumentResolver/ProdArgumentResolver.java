package heehunjun.playground.config.argumentResolver;

import heehunjun.playground.controller.tool.token.jwt.JwtManager;
import heehunjun.playground.exception.HhjClientException;
import heehunjun.playground.exception.code.ClientErrorCode;
import heehunjun.playground.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
public class ProdArgumentResolver extends MemberArgumentResolver {

    public ProdArgumentResolver(JwtManager jwtManager, AuthService authService) {
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

        String email = jwtManager.extractAccessToken(accessToken);
        return authService.getMemberByEmail(email);
    }
}
