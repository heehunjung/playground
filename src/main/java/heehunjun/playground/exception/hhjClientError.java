package heehunjun.playground.exception;

import org.springframework.http.HttpStatus;

public class hhjClientError extends hhjException {

    public hhjClientError(String message, HttpStatus status) {
        super(message, status);
    }
}
