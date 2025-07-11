package heehunjun.playground.config;

import heehunjun.playground.config.argumentResolver.LocalMemberArgumentResolver;
import heehunjun.playground.config.argumentResolver.ProdMemberArgumentResolver;
import heehunjun.playground.controller.tool.token.jwt.JwtManager;
import heehunjun.playground.service.auth.AuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class ArgumentResolverConfig {

    @Profile({"dev", "test", "default"})
    @Configuration
    public static class LocalArgumentResolverConfig {

        @Bean
        public LocalMemberArgumentResolver localArgumentResolver(JwtManager jwtManager,
                                                                 AuthService authService) {
            return new LocalMemberArgumentResolver(jwtManager, authService);
        }
    }

    @Profile({"prod"})
    @Configuration
    public static class consoleLoggerConfig {

        @Bean
        public ProdMemberArgumentResolver prodArgumentResolver(JwtManager jwtManager,
                                                               AuthService authService) {
            return new ProdMemberArgumentResolver(jwtManager, authService);
        }
    }
}
