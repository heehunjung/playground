package heehunjun.playground.global.exception;

import org.springframework.http.HttpStatus;

public class hhjClientError extends hhjException {

    public hhjClientError(String message, HttpStatus status) {
        super(message, status);
    }
}
