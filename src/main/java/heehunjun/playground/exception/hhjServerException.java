package heehunjun.playground.exception;

import org.springframework.http.HttpStatus;

public class hhjServerException extends hhjException {

    public hhjServerException(String message, HttpStatus status) {
        super(message, status);
    }
}
