package heehunjun.playground.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HhjException extends RuntimeException {

    private final HttpStatus status;

    public HhjException(final String message, final HttpStatus status) {
        super(message);
        this.status = status;
    }
}
