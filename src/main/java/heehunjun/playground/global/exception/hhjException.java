package heehunjun.playground.global.exception;

import org.springframework.http.HttpStatus;

public class hhjException extends RuntimeException {

    private HttpStatus status;

    public hhjException(final String message, final HttpStatus status) {
        // 생성자가 가장 위에 와야함
        super(message);
        this.status = status;
    }
}
