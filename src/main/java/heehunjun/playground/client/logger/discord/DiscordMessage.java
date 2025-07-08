package heehunjun.playground.client.logger.discord;

import java.util.List;

public record DiscordMessage(String content, List<Embed> embeds) {
    public record Embed(String title, String description) {}
}
