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
    _TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "T404", "토큰을 찾을 수 없습니다."),
    _TOKEN_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "T500", "토큰 생성에 실패했습니다."),

    // mail error
    _MAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "M404", "존재하지 않는 이메일입니다."),
    _MAIL_CONFLICT(HttpStatus.CONFLICT, "M409", "이미 인증된 이메일입니다."),
    _MAIL_INCORRECT(HttpStatus.UNAUTHORIZED, "M400", "인증 번호가 올바르지 않습니다."),
    _MAIL_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "M401", "인증 시간이 만료되었습니다. 다시 인증을 요청해 주세요."),

    //user error
    _LOGIN_FAILURE(HttpStatus.UNAUTHORIZED, "U401", "아이디 또는 비밀번호가 올바르지 않습니다."),
    _LOGINID_CONFLICT(HttpStatus.CONFLICT, "U409", "이미 존재하는 아이디입니다."),
    _NICKNAME_CONFLICT(HttpStatus.CONFLICT, "U409", "이미 존재하는 닉네임입니다."),
    _USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U404", "사용자를 찾을 수 없습니다."),
    _PARTICIPATE_NOT_FOUND(HttpStatus.NOT_FOUND, "A404", "신청하지 않은 사용자입니다."),
    _PARTICIPATE_CONFLICT(HttpStatus.CONFLICT, "A409", "이미 신청한 공구입니다."),
    _IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "U405", "이미지를 찾을 수 없습니다."),


    //post error
    _POST_BAD_REQUEST(HttpStatus.BAD_REQUEST, "P400", "조회할 수 있는 게시글이 없습니다."),
    _POST_NOT_FOUND(HttpStatus.NOT_FOUND, "P404", "게시글이 존재하지 않습니다."),
    _POST_FORBIDDEN(HttpStatus.FORBIDDEN, "P403", "해당 게시글의 권한이 없습니다."),
    _POST_NOT_PARTICIPATE(HttpStatus.BAD_REQUEST, "P400", "해당 공구에 참여할 수 없습니다."),
    _POST_CONFLICT(HttpStatus.CONFLICT, "P409", "이미 마감된 공구입니다."),

    //inventory error
    _INVENTORY_INVALID_FILTER(HttpStatus.BAD_REQUEST, "C400", "조회할 수 있는 재고가 없습니다."),
    _INVENTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "I404", "재고를 찾을 수 없습니다."),
    _CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "C404", "해당 카테고리를 찾을 수 없습니다."),
    _CATEGORY_DUPLICATE(HttpStatus.CONFLICT, "C409", "해당 재고에 이미 같은 이름의 카테고리가 존재합니다."),
    _CATEGORY_NOT_IN_INVENTORY(HttpStatus.NOT_FOUND, "C408", "해당 재고에 존재하지 않는 카테고리입니다."),




    //search error
    _SEARCH_NOT_FOUND(HttpStatus.NOT_FOUND,"F404","검색 기록이 존재하지 않습니다.")
    ;




    private final HttpStatus status;
    private final String code;
    private final String message;
}
