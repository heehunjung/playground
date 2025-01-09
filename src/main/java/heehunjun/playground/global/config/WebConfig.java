package heehunjun.playground.global.config;

import heehunjun.playground.domain.auth.interceptor.MyInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final MyInterceptor myInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myInterceptor)
                .order(1)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/oauth2/**")
                .excludePathPatterns("/api/token/reissue");
    }
}
