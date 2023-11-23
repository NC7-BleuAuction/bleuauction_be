package bleuauction.bleuauction_be.server.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseException extends RuntimeException {

  private BaseErrorCode errorCode;
}
