package heehunjun.playground.domain.check;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api")
public class TempController {

    @GetMapping("/")
    public String message() {
        return "재탕 아니라고 했다.";
    }

    @GetMapping("/health")
    public String health() {
        return "건강해요.";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
