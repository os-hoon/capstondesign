package cherrysumer.cherrysumer.exception.handler;

import cherrysumer.cherrysumer.exception.BaseException;
import cherrysumer.cherrysumer.exception.ErrorCode;

public class UserErrorHandler extends BaseException {
    public UserErrorHandler(String message, ErrorCode code) {
        super(message, code);
    }

    public UserErrorHandler(ErrorCode code) {
        super(code);
    }
}
