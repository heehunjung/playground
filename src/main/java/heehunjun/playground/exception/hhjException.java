package heehunjun.playground.exception;

import org.springframework.http.HttpStatus;

public class hhjException extends RuntimeException {

    private final HttpStatus status;

    public hhjException(final String message, final HttpStatus status) {
        super(message);
        this.status = status;
    }
}
