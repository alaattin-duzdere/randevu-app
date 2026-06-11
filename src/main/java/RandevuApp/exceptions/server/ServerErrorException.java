package RandevuApp.exceptions.server;

import RandevuApp.api.ErrorCode;
import RandevuApp.exceptions.BaseApiException;

public class ServerErrorException extends BaseApiException {

    /**
     * @param message Hatayı açıklayan ve loglanacak olan spesifik mesaj.
     */
    public ServerErrorException(String message) {
        super(ErrorCode.ERROR_SERVER_GENERAL, message);
    }

    /**
     * @param message Hatayı açıklayan ve loglanacak olan spesifik mesaj.
     * @param cause Orijinal hata (root cause), loglama ve hata ayıklama için.
     */
    public ServerErrorException(String message, Throwable cause) {
        super(ErrorCode.ERROR_SERVER_GENERAL, message, cause);
    }
}
