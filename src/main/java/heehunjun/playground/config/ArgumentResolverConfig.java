package heehunjun.playground.config;

import heehunjun.playground.client.alert.discord.DiscordProperties;
import heehunjun.playground.config.argumentResolver.LocalArgumentResolver;
import heehunjun.playground.config.argumentResolver.ProdArgumentResolver;
import heehunjun.playground.controller.tool.token.jwt.JwtManager;
import heehunjun.playground.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class ArgumentResolverConfig {

    @Profile({"dev", "default", "test"})
    @Configuration
    @RequiredArgsConstructor
    @EnableConfigurationProperties(DiscordProperties.class)
    public static class LocalArgumentResolverConfig {

        @Bean
        public LocalArgumentResolver localArgumentResolver(JwtManager jwtManager,
                                                           AuthService authService) {
            return new LocalArgumentResolver(jwtManager, authService);
        }
    }

    @Profile({"prod"})
    @Configuration
    public static class consoleLoggerConfig {

        @Bean
        public ProdArgumentResolver prodArgumentResolver(JwtManager jwtManager,
                                                         AuthService authService) {
            return new ProdArgumentResolver(jwtManager, authService);
        }
    }
}
