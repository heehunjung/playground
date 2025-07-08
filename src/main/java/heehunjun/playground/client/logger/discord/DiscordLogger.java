package heehunjun.playground.client.logger.discord;

import heehunjun.playground.client.logger.Logger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
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
                .bodyToMono(Void.class) // Discord 는 성공 시 204 No Content
                .onErrorResume(e -> {
                    log.error("{}", e);
                    return Mono.empty();
                });
    }

    private DiscordMessage getDiscordMessage(Throwable e) {
        String content = "🚨 에러 발생";

        String title = "💥 예외 타입: " + e.getClass().getSimpleName();
        String message = String.format(
                """
                📝 메시지     : %s
                ⏰ 발생 시각 : %s
                📌 스택 트레이스:
                %s
                """,
                e.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                getStackTraceAsString(e)
        );

        DiscordMessage.Embed embed = new DiscordMessage.Embed(title, message);
        return new DiscordMessage(content, List.of(embed));
    }


    private String getStackTraceAsString(Throwable e) {
        StringBuilder stackTrace = new StringBuilder();
        stackTrace.append("```\n")
                .append(e.toString())
                .append("\n```");
        // Discord 메세지 길이 제한
        StackTraceElement[] elements = e.getStackTrace();
        int limit = Math.min(elements.length, 10);
        for (int i = 0; i < limit; i++) {
            stackTrace.append("```\n")
                    .append(elements[i])
                    .append("\n```");
        }

        return stackTrace.toString();
    }
}
