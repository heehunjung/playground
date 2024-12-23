package heehunjun.playground.temp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TempController {
    
    @GetMapping("/")
    public String message() {
        return "재탕 아니라고 했다.";
    }
}
