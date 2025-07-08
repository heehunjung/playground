package heehunjun.playground.exception;

import org.springframework.http.HttpStatus;

public class HhjServerException extends HhjException {
    public HhjServerException(String message, HttpStatus status) {
        super(message, status);
    }
}
