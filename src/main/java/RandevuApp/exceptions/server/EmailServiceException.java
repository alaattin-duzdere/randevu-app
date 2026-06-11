package RandevuApp.exceptions.server;

import RandevuApp.api.ErrorCode;
import RandevuApp.exceptions.BaseApiException;

public class EmailServiceException extends BaseApiException {

    /**
     * Constructs a new EmailServiceException.
     * @param message A descriptive message about the failure.
     * @param cause The original exception that was caught (e.g., MailException), for logging purposes.
     */
    public EmailServiceException(String message, Throwable cause) {
        super(ErrorCode.ERROR_EMAIL_SERVICE_FAILURE, message, cause);
    }

    public EmailServiceException(String message) {
        super(ErrorCode.ERROR_EMAIL_SERVICE_FAILURE,message);
    }
}