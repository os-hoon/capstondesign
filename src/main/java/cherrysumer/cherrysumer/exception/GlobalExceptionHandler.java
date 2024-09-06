package cherrysumer.cherrysumer.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /*@ExceptionHandler(HttpRequestMethodNotSupportedException.class) // HttpRequestMethodNotSupportedException 예외를 잡아서 처리
    protected ResponseEntity<ErrorResponse> handle(HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException", e);
        return createErrorResponseEntity(ErrorCode._INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> createErrorResponseEntity(ErrorCode errorCode) {
        return new ResponseEntity<>(
                ErrorResponse.of(errorCode),
                errorCode.getStatus());
    }*/
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        String message = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> Optional.ofNullable(fieldError.getDefaultMessage()).orElse(""))
                .findFirst()
                .orElse("No errors");

        return handleExceptionInternalArgs(e,HttpHeaders.EMPTY,ErrorCode.valueOf("_BAD_REQUEST"),request,message);
    }

    @ExceptionHandler
    public ResponseEntity<Object> exception(Exception e, WebRequest request) {
        e.printStackTrace();

        return handleExceptionInternalFalse(e, ErrorCode._INTERNAL_SERVER_ERROR, HttpHeaders.EMPTY, ErrorCode._INTERNAL_SERVER_ERROR.getStatus(),request, e.getMessage());
    }

    @ExceptionHandler(value = BaseException.class)
    public ResponseEntity onThrowException(BaseException baseException, HttpServletRequest request) {
        ErrorCode code = baseException.getErrorCode();
        ErrorResponse reason = ErrorResponse.of(code);
        return handleExceptionInternal(baseException,reason,code,null,request);
    }

    private ResponseEntity<Object> handleExceptionInternal(Exception e, ErrorResponse reason, ErrorCode code,
                                                           HttpHeaders headers, HttpServletRequest request) {

//        e.printStackTrace();

        WebRequest webRequest = new ServletWebRequest(request);
        return super.handleExceptionInternal(
                e,
                reason,
                headers,
                code.getStatus(),
                webRequest
        );
    }

    private ResponseEntity<Object> handleExceptionInternalFalse(Exception e, ErrorCode errorCommonStatus,
                                                                HttpHeaders headers, HttpStatus status, WebRequest request, String errorPoint) {
        ErrorResponse body = ErrorResponse.of(errorCommonStatus);

        return super.handleExceptionInternal(
                e,
                body,
                headers,
                status,
                request
        );
    }

    private ResponseEntity<Object> handleExceptionInternalArgs(Exception e, HttpHeaders headers, ErrorCode errorCommonStatus,
                                                               WebRequest request, String message) {
        ErrorResponse body = ErrorResponse.of(errorCommonStatus, message);

        return super.handleExceptionInternal(
                e,
                body,
                headers,
                errorCommonStatus.getStatus(),
                request
        );
    }

    private ResponseEntity<Object> handleExceptionInternalConstraint(Exception e, ErrorCode errorCommonStatus,
                                                                     HttpHeaders headers, WebRequest request) {
        ErrorResponse body = ErrorResponse.of(errorCommonStatus);

        return super.handleExceptionInternal(
                e,
                body,
                headers,
                errorCommonStatus.getStatus(),
                request
        );
    }
}
