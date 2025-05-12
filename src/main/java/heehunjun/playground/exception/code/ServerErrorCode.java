package heehunjun.playground.exception.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ServerErrorCode implements ErrorResponse {
    ;

    private HttpStatus httpStatus;
    private String message;

    ServerErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
