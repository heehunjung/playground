package heehunjun.playground;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

public class projectEnvTest {
    @Value("${dev.test}")
    String env;

    @Test
    void 원하는_환경의_yml파일을_사용한다() {
        Assertions.assertThat(env).isEqualTo("dev");
    }
}
