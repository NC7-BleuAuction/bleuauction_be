package bleuauction.bleuauction_be.server.common.exception;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import bleuauction.bleuauction_be.server.common.dto.ErrorDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GlobalException implements BaseErrorCode {
    EXPIRED_TOKEN_EXCEPTION(UNAUTHORIZED.value(), "E", "토큰이 만료되었습니다."),
    INVALID_TOKEN_EXCEPTION(UNAUTHORIZED.value(), "I", "올바르지 않은 토큰입니다."),
    RENEW_ACCESS_TOKEN_EXCEPTION(UNAUTHORIZED.value(), "RATE", "AccesToken 재발급 에러입니다."),
    INTERNAL_SERVER_ERRORS(INTERNAL_SERVER_ERROR.value(), "SE", "서버 내부 오류입니다."),
    FORBIDDEN_ACCESS_EXCEPTION(FORBIDDEN.value(), "403-1", "내부 서버 403 오류 입니다."),
    MENU_NOT_FOUND_EXCEPTION(NOT_FOUND.value(), "404-01", "메뉴를 찾을 수 없습니다."),
    NOTICE_NOT_FOUND_EXCEPTION(NOT_FOUND.value(), "404-02", "공지사항을 찾을 수 없습니다."),
    ORDER_MENU_NOT_FOUND_EXCEPTION(NOT_FOUND.value(), "404-03", "주문내역 찾을 수 없습니다."),
    ;

    private final Integer statusCode;
    private final String errorCode;
    private final String reason;

    @Override
    public ErrorDetail getErrorDetail() {
        return ErrorDetail.of(statusCode, errorCode, reason);
    }
}
