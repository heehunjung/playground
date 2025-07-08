package heehunjun.playground.exception;

import heehunjun.playground.client.logger.Logger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionController {

    private final Logger logger;

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleRuntimeException(Exception exception) {
        serverLog(exception);

        return ResponseEntity.internalServerError()
                .body("서버 에러 관리자에게 문의해주세요.");
    }

    @ExceptionHandler(HhjServerException.class)
    public ResponseEntity handleServerException(HhjServerException exception) {
        serverLog(exception);

        return ResponseEntity.status(exception.getStatus())
                .body(exception.getMessage());
    }

    @ExceptionHandler(HhjClientException.class)
    public ResponseEntity handleClientException(HhjClientException exception) {
        clientLog(exception);

        return ResponseEntity.status(exception.getStatus())
                .body(exception.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity handleNoResourceFoundException(NoResourceFoundException exception) {
        clientLog(exception);

        return ResponseEntity.status(exception.getStatusCode())
                .body(exception.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException exception) {
        clientLog(exception);

        return ResponseEntity.status(exception.getStatusCode())
                .body(exception.getMessage());
    }
    // 추가 예정

    private void clientLog(Exception exception) {
        log.warn("client exception: {}", exception.getMessage());
    }

    private void serverLog(Exception exception) {
        logger.log(exception);
    }
}
