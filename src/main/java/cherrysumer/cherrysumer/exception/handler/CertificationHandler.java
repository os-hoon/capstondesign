package cherrysumer.cherrysumer.exception.handler;

import cherrysumer.cherrysumer.exception.BaseException;
import cherrysumer.cherrysumer.exception.ErrorCode;

public class CertificationHandler extends BaseException {
    public CertificationHandler(String message, ErrorCode code) {
        super(message, code);
    }

    public CertificationHandler(ErrorCode code) {
        super(code);
    }
}
