package heehunjun.playground.exception.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ClientErrorCode implements ErrorResponse {

    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "로그인 유효시간이 만료되었습니다."),
    UNAUTHORIZED_MEMBER(HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다.");

    private HttpStatus httpStatus;
    private String message;

    ClientErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
