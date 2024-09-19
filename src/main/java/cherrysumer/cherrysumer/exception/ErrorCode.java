package cherrysumer.cherrysumer.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "server error"),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "400", "잘못된 요청입니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "403", "접근 권한이 없습니다."),
    _CONFLICT(HttpStatus.CONFLICT, "409", "중복된 데이터 요청입니다."),

    // token error
    _TOKEN_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "T401", "유효하지 않은 토큰입니다."),
    _TOKEN_EXPIRATION(HttpStatus.UNAUTHORIZED, "T401", "만료된 토큰입니다."),
    _TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "T404", "존재하지 않는 토큰입니다."),
    _TOKEN_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "T500", "토큰 생성에 실패했습니다."),

    // mail error
    _MAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "M404", "존재하지 않는 이메일입니다."),
    _MAIL_CONFLICT(HttpStatus.CONFLICT, "M409", "이미 인증된 이메일입니다."),
    _MAIL_INCORRECT(HttpStatus.UNAUTHORIZED, "M400", "인증 번호가 올바르지 않습니다."),
    _MAIL_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "M401", "인증 시간이 만료되었습니다. 다시 인증을 요청해 주세요."),

    //user error
    _LOGINID_CONFLICT(HttpStatus.CONFLICT, "U409", "이미 존재하는 아이디입니다."),
    _NICKNAME_CONFLICT(HttpStatus.CONFLICT, "U409", "이미 존재하는 닉네임입니다."),
    _USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U404", "아이디 또는 비밀번호가 올바르지 않습니다."),

    //inventory error
    _INVENTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "I404", "재고를 찾을 수 없습니다."),
    ;



    private final HttpStatus status;
    private final String code;
    private final String message;
}
