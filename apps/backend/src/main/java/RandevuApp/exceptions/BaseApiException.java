package RandevuApp.exceptions;

import RandevuApp.api.ErrorCode;
import lombok.Getter;

@Getter
public abstract class BaseApiException extends RuntimeException {

    private final ErrorCode errorCode;

    public BaseApiException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    // Optional: for cases where no message is provided
    public BaseApiException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

    // New constructor accepting a Throwable cause
    public BaseApiException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getDefaultMessage(), cause);
        this.errorCode = errorCode;
    }

    public BaseApiException(ErrorCode errorCode, String message, Throwable cause) {
        super(message != null ? message : errorCode.getDefaultMessage(), cause);
        this.errorCode = errorCode;
    }

}