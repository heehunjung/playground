package heehunjun.playground.client.alert.discord;

import heehunjun.playground.client.alert.Logger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class DiscordLogger implements Logger {

    private final DiscordProperties discordProperties;
    private final WebClient webClient;

    public DiscordLogger(WebClient.Builder webClientBuilder, DiscordProperties discordProperties) {
        this.discordProperties = discordProperties;
        this.webClient = webClientBuilder
                .baseUrl(discordProperties.getWebhookUrl())
                .build();
    }

    @Override
    public void log(Throwable e) {
        DiscordMessage discordMessage = getDiscordMessage(e);
        sendWebhook(discordMessage)
                .subscribe();
    }

    private Mono<Void> sendWebhook(DiscordMessage discordMessage) {
        return webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(discordMessage)
                .retrieve()
                .bodyToMono(Void.class); // Discord 는 성공 시 204 No Content 반환
                // 실패하면 ? -> 서버 에러로그 따로 저장 ?
    }

    private DiscordMessage getDiscordMessage(Throwable e) {
        String content = "🚨 에러 발생";
        DiscordMessage.Embed embed = new DiscordMessage.Embed(
                "Error: " + e.getClass().getSimpleName(),
                String.format(
                        "Message: %s\nTime: %s\nStackTrace: %s",
                        e.getMessage(),
                        LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                        getStackTraceAsString(e)
                )
        );
        return new DiscordMessage(content, List.of(embed));
    }

    private String getStackTraceAsString(Throwable e) {
        StringBuilder stackTrace = new StringBuilder();
        stackTrace.append(e.toString()).append("\n");
        for (StackTraceElement element : e.getStackTrace()) {
            stackTrace.append("\tat ").append(element.toString()).append("\n");
        }

        return stackTrace.length() > 1500 ? stackTrace.substring(0, 1500) + "..." : stackTrace.toString();
    }
}
