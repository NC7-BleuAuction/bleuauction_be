package bleuauction.bleuauction_be.server.exception;

import static bleuauction.bleuauction_be.server.exception.GlobalException.RENEW_ACCESS_TOKEN_EXCEPTION;


public class RenewAccessTokenException extends BaseException {

  public static final RenewAccessTokenException EXCEPTION = new RenewAccessTokenException();

  public RenewAccessTokenException() {
    super(RENEW_ACCESS_TOKEN_EXCEPTION);
  }
}
