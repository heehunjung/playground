package heehunjun.playground.domain.auth.oauth.service.google;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@AllArgsConstructor
public class GoogleTokenResponse {
    private String accessToken;
    private String refreshToken;
    private String idToken;
}
