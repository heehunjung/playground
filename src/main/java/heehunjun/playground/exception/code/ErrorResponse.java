package heehunjun.playground.exception.code;

import org.springframework.http.HttpStatus;

public interface ErrorResponse {

    HttpStatus getHttpStatus();

    String getMessage();
}
