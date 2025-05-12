package heehunjun.playground.exception;

import heehunjun.playground.exception.code.ClientErrorCode;

public class HhjClientException extends HhjException {

    public HhjClientException(ClientErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode.getHttpStatus());
    }
}
