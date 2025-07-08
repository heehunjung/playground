package heehunjun.playground.config;

import heehunjun.playground.config.argumentResolver.MemberArgumentResolver;
import heehunjun.playground.interceptor.AuthInterceptor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final MemberArgumentResolver memberArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .order(1)
                .excludePathPatterns("/**");
//                .addPathPatterns("/api/**")
//                .excludePathPatterns("/api/member")
//                .excludePathPatterns("/api/token/reissue");

    }
}

