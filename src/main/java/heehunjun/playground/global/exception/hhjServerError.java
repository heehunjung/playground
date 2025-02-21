package heehunjun.playground.global.exception;

import org.springframework.http.HttpStatus;

public class hhjServerError extends hhjException {

    public hhjServerError(String message, HttpStatus status) {
        super(message, status);
    }
}
