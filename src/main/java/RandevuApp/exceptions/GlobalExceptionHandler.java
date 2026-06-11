package RandevuApp.exceptions;

import RandevuApp.api.ErrorCode;
import RandevuApp.api.ApiErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Helper method
    private ResponseEntity<ApiErrorResponse> buildErrorResponse(ErrorCode errorCode, ApiErrorResponse apiErrorResponse) {
        return new ResponseEntity<>(apiErrorResponse, errorCode.getHttpStatus());
    }

    // 1. CORE & BUSINESS EXCEPTIONS

    @ExceptionHandler(BaseApiException.class)
    public ResponseEntity<ApiErrorResponse> handleBaseApiException(BaseApiException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ApiErrorResponse body = new ApiErrorResponse(errorCode, ex.getMessage());
        return buildErrorResponse(errorCode, body);
    }

    // 2. SECURITY & AUTHENTICATION EXCEPTIONS

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiErrorResponse> handleExpiredJwtException(ExpiredJwtException ex) {
        ApiErrorResponse body = new ApiErrorResponse(
                ErrorCode.ERROR_EXPIRED_TOKEN,
                "Your session has expired. Please log in again."
        );
        return buildErrorResponse(ErrorCode.ERROR_EXPIRED_TOKEN, body);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ApiErrorResponse> handleMalformedJwtException(MalformedJwtException ex) {
        ApiErrorResponse body = new ApiErrorResponse(
                ErrorCode.ERROR_INVALID_TOKEN,
                "The provided token is malformed."
        );
        return buildErrorResponse(ErrorCode.ERROR_INVALID_TOKEN, body);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ApiErrorResponse> handleSignatureException(SignatureException ex) {
        ApiErrorResponse body = new ApiErrorResponse(
                ErrorCode.ERROR_INVALID_TOKEN,
                "The provided token has an invalid signature."
        );
        return buildErrorResponse(ErrorCode.ERROR_INVALID_TOKEN, body);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        ApiErrorResponse body = new ApiErrorResponse(
                ErrorCode.ERROR_USER_NOT_FOUND,
                "The specified user does not exist."
        );
        return buildErrorResponse(ErrorCode.ERROR_USER_NOT_FOUND, body);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDeniedForMethodSecurity() {
        ApiErrorResponse body = new ApiErrorResponse(
                ErrorCode.ERROR_FORBIDDEN,
                "Access denied"
        );
        return buildErrorResponse(ErrorCode.ERROR_FORBIDDEN, body);
    }

    // 3. VALIDATION & INPUT HANDLING

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptionsSimple(MethodArgumentNotValidException ex) {
        String combinedErrorMessage = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        ApiErrorResponse body = new ApiErrorResponse(
                ErrorCode.ERROR_INVALID_INPUT,
                combinedErrorMessage
        );

        return buildErrorResponse(ErrorCode.ERROR_INVALID_INPUT, body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadableException() {
        ApiErrorResponse body = new ApiErrorResponse(
                ErrorCode.ERROR_BAD_REQUEST,
                "The request body is malformed or unreadable."
        );
        return buildErrorResponse(ErrorCode.ERROR_BAD_REQUEST, body);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = String.format("The parameter '%s' of value '%s' could not be converted to type '%s'", ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

        ApiErrorResponse body = new ApiErrorResponse(ErrorCode.ERROR_BAD_REQUEST, message);
        return buildErrorResponse(ErrorCode.ERROR_BAD_REQUEST, body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        List<String> details = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());

        log.warn("Constraint validation error: {}", details);

        ErrorCode errorCode = ErrorCode.VALIDATION_ERROR;
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(errorCode, String.join(", ", details));
        return buildErrorResponse(errorCode, apiErrorResponse);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex) {
        ApiErrorResponse body = new ApiErrorResponse(
                ErrorCode.ERROR_UNSUPPORTED_FILE_TYPE,
                "Content type '" + ex.getContentType() + "' not supported"
        );
        return buildErrorResponse(ErrorCode.ERROR_UNSUPPORTED_FILE_TYPE, body);
    }

    // 4. DATABASE & DATA INTEGRITY

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        // Not: Burada unique constraint hataları (örn: aynı email ile kayıt) genelde yakalanır.
        ApiErrorResponse body = new ApiErrorResponse(
                ErrorCode.ERROR_DATA_INTEGRITY_VIOLATION,
                "Data integrity violation. This implies a conflict (e.g., duplicate entry)."
        );
        return buildErrorResponse(ErrorCode.ERROR_DATA_INTEGRITY_VIOLATION, body);
    }

    // 5. WEB & HTTP INFRASTRUCTURE

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleRequestMethodNotSupportedException() {
        ApiErrorResponse body = new ApiErrorResponse(
                ErrorCode.ERROR_METHOD_NOT_ALLOWED,
                "The request method is not supported for this endpoint."
        );
        return buildErrorResponse(ErrorCode.ERROR_METHOD_NOT_ALLOWED, body);
    }

    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    public ResponseEntity<ApiErrorResponse> handleNotFoundExceptions() {
        ApiErrorResponse body = new ApiErrorResponse(
                ErrorCode.ERROR_NOT_FOUND,
                "The requested resource was not found."
        );
        return buildErrorResponse(ErrorCode.ERROR_NOT_FOUND, body);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiErrorResponse> handleMaxUploadSizeEx() {
        ApiErrorResponse body = new ApiErrorResponse(
                ErrorCode.ERROR_PAYLOAD_TOO_LARGE,
                "The requested body was over the max size limit."
        );
        return buildErrorResponse(ErrorCode.ERROR_PAYLOAD_TOO_LARGE, body);
    }

    // 6. GLOBAL FALLBACK

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex) {
        ErrorCode errorCode = ErrorCode.ERROR_INTERNAL_SERVER;
        log.error("Unhandled Global Exception occurred:", ex);

        ApiErrorResponse body = new ApiErrorResponse(
                errorCode,
                errorCode.getDefaultMessage()
        );

        return buildErrorResponse(errorCode, body);
    }
}