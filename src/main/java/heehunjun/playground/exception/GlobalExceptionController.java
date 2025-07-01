package heehunjun.playground.exception;

import heehunjun.playground.client.alert.Logger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
    // 추가 예정

    private void clientLog(Exception exception) {
        log.warn("client exception: {}", exception.getMessage());
    }

    private void serverLog(Exception exception) {
        log.error("server exception: {}", exception.getMessage());
        logger.log(exception);
    }}
