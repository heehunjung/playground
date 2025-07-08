package heehunjun.playground;

import heehunjun.playground.exception.HhjServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HealthController {

    @GetMapping("/")
    public String message() {
        return "재탕 아니라고 했다.";
    }

    @GetMapping("/health")
    public String health() {
        return "건강해요.";
    }

    @GetMapping("/server")
    public void loginPage() {
        throw new HhjServerException("test123", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
