package bleuauction.bleuauction_be.server.exception;

import static bleuauction.bleuauction_be.server.exception.GlobalException.EXPIRED_TOKEN_EXCEPTION;


public class ExpiredTokenException extends BaseException {

  public static final ExpiredTokenException EXCEPTION = new ExpiredTokenException();

  public ExpiredTokenException() {
    super(EXPIRED_TOKEN_EXCEPTION);
  }
}
