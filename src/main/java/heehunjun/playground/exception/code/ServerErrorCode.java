package heehunjun.playground.exception.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ServerErrorCode implements ErrorResponse {

    //각 필드별로 바인딩해도 괜찮을듯
    PROPERTIES_BINDING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "프로퍼티 값들을 불러오지 못했습니다.");


    private HttpStatus httpStatus;
    private String message;

    ServerErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
