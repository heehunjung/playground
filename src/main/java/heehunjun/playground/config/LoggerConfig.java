package heehunjun.playground.config;

import heehunjun.playground.client.logger.ConsoleLogger;
import heehunjun.playground.client.logger.Logger;
import heehunjun.playground.client.logger.discord.DiscordLogger;
import heehunjun.playground.client.logger.discord.DiscordProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class LoggerConfig {

    @Profile({"dev", "prod"})
    @Configuration
    @RequiredArgsConstructor
    @EnableConfigurationProperties(DiscordProperties.class)
    public static class DiscordLoggerConfig {

        private final DiscordProperties discordProperties;

        @Bean
        public Logger discordLogger() {
            return new DiscordLogger(WebClient.builder(), discordProperties);
        }
    }

    @Profile({"test", "default"})
    @Configuration
    public static class consoleLoggerConfig {

        @Bean
        public Logger consoleLogger() {
            return new ConsoleLogger();
        }
    }
}
