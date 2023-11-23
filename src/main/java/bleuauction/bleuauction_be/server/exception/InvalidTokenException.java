package bleuauction.bleuauction_be.server.exception;

import static bleuauction.bleuauction_be.server.exception.GlobalException.INVALID_TOKEN_EXCEPTION;


public class InvalidTokenException extends BaseException {

  public static final InvalidTokenException EXCEPTION = new InvalidTokenException();

  public InvalidTokenException() {
    super(INVALID_TOKEN_EXCEPTION);
  }
}
