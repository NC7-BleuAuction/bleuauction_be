package bleuauction.bleuauction_be.server.exception;

import bleuauction.bleuauction_be.server.common.dto.ErrorDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Getter
@AllArgsConstructor
public enum GlobalException implements BaseErrorCode {
  EXPIRED_TOKEN_EXCEPTION(UNAUTHORIZED.value(), "E", "토큰이 만료되었습니다."),
  INVALID_TOKEN_EXCEPTION(UNAUTHORIZED.value(), "I", "올바르지 않은 토큰입니다."),
  RENEW_ACCESS_TOKEN_EXCEPTION(UNAUTHORIZED.value(), "RATE", "AccesToken 재발급 에러입니다."),
  INTERNAL_SERVER_ERRORS(INTERNAL_SERVER_ERROR.value(), "SE", "서버 내부 오류입니다.");

  private final Integer statusCode;
  private final String errorCode;
  private final String reason;

  @Override
  public ErrorDetail getErrorDetail() {
    return ErrorDetail.of(statusCode, errorCode, reason);
  }
}