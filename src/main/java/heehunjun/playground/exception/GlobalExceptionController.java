package heehunjun.playground.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleRuntimeException(Exception exception) {
        exception.printStackTrace();

        return ResponseEntity.internalServerError()
                .body("서버 에러 관리자에게 문의해주세요.");
    }

    @ExceptionHandler(HhjServerException.class)
    public ResponseEntity handleServerException(HhjServerException exception) {
        exception.printStackTrace();

        return ResponseEntity.status(exception.getStatus())
                .body(exception.getMessage());
    }

    @ExceptionHandler(HhjClientException.class)
    public ResponseEntity handleClientException(HhjClientException exception) {
        log.error("ClientErrorMessage: {}", exception.getMessage());

        return ResponseEntity.status(exception.getStatus())
                .body(exception.getMessage());
    }
    // 추가 예정
}
