package heehunjun.playground.config;

import heehunjun.playground.client.auth.google.GoogleProperties;
import heehunjun.playground.client.auth.kakao.KakaoProperties;
import heehunjun.playground.controller.tool.token.jwt.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({KakaoProperties.class, GoogleProperties.class, JwtProperties.class})
public class BindingConfig {
}
