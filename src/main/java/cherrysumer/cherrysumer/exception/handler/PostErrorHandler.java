package cherrysumer.cherrysumer.exception.handler;

import cherrysumer.cherrysumer.exception.BaseException;
import cherrysumer.cherrysumer.exception.ErrorCode;

public class PostErrorHandler extends BaseException {
    public PostErrorHandler(String message, ErrorCode code) {
        super(message, code);
    }

    public PostErrorHandler(ErrorCode code) {
        super(code);
    }
}
