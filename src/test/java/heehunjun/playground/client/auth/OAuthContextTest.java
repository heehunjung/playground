package heehunjun.playground.client.auth;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig
@Import({OAuthClient.class, OAuthContext.class})
class OAuthContextTest {

    @Test
    void aa() {

    }
}
