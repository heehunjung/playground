package heehunjun.playground.config.argumentResolver;

import heehunjun.playground.controller.auth.AuthMember;
import heehunjun.playground.controller.tool.token.jwt.JwtManager;
import heehunjun.playground.exception.HhjClientException;
import heehunjun.playground.exception.code.ClientErrorCode;
import heehunjun.playground.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public abstract class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    protected final JwtManager jwtManager;
    protected final AuthService authService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthMember.class);
    }

    @Override
    public abstract Object resolveArgument(MethodParameter parameter,
                                           ModelAndViewContainer mavContainer,
                                           NativeWebRequest webRequest,
                                           WebDataBinderFactory binderFactory) throws Exception;
}
