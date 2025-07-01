package heehunjun.playground.client.alert.discord;

import java.util.List;

public record DiscordMessage(String content, List<Embed> embeds) {
    public record Embed(String title, String description) {}
}
