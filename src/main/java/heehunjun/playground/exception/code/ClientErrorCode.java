package heehunjun.playground.exception.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ClientErrorCode implements ErrorResponse {

    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "로그인 유효시간이 만료되었습니다."),
    UNAUTHORIZED_MEMBER(HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다."),

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 멤버를 찾을 수 없습니다."),

    // 대충..
    INVALID_TITLE_LENGTH(HttpStatus.BAD_REQUEST, "제목 길이가 잘못되었습니다."),
    INVALID_CONTENT_LENGTH(HttpStatus.BAD_REQUEST, "내용 길이가 잘못되었습니다.");

    private HttpStatus httpStatus;
    private String message;

    ClientErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
