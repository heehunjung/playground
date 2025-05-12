package heehunjun.playground.exception;

import heehunjun.playground.exception.code.ClientErrorCode;
import org.springframework.http.HttpStatus;

public class hhjClientException extends hhjException {

    public hhjClientException(ClientErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode.getHttpStatus());
    }
}
