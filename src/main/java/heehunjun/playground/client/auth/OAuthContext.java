package heehunjun.playground.client.auth;

import heehunjun.playground.dto.member.MemberInfo;
import heehunjun.playground.dto.member.OauthToken;
import heehunjun.playground.exception.HhjServerException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OAuthContext {

    private final Map<String, OAuthClient> oauthClients;

    OAuthContext(Map<String, OAuthClient> oauthClients) {
        this.oauthClients = Map.copyOf(oauthClients);
    }

    public OauthToken getOauthToken(OAuthType type, String code) {
        OAuthClient client = getClient(type);

        return client.getOauthToken(code);
    }

    public MemberInfo getMemberInfo(OAuthType type, OauthToken request) {
        OAuthClient client = getClient(type);

        log.info("token {}", request.accessToken());
        return client.getMemberInfo(request.accessToken());
    }

    private OAuthClient getClient(OAuthType type) {
        OAuthClient client = oauthClients.getOrDefault(type.name(), null);
        if (client == null) {
            throw new HhjServerException("aaa", HttpStatus.UNAUTHORIZED); // 수정 예정
        }
        return client;
    }
}
