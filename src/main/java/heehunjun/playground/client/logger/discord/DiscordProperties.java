package heehunjun.playground.client.logger.discord;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;

@Profile({"dev", "prod"})
@Getter
@ConfigurationProperties(prefix = "logging.discord-error")
public class DiscordProperties {

    private final String webhookUrl;

    public DiscordProperties(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }
}
