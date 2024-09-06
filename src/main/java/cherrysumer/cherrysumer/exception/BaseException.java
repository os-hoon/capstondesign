package cherrysumer.cherrysumer.exception;

public class BaseException extends RuntimeException{

    private final ErrorCode code;

    public BaseException(String message, ErrorCode code) {
        super(message);
        this.code = code;
    }

    public BaseException(ErrorCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public ErrorCode getErrorCode() {
        return code;
    }
}
