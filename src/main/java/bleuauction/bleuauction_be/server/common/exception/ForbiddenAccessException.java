package bleuauction.bleuauction_be.server.common.exception;


public class ForbiddenAccessException extends BaseException {

  public static final ForbiddenAccessException EXCEPTION = new ForbiddenAccessException();

  public ForbiddenAccessException() {
    super(GlobalException.FORBIDDEN_ACCESS_EXCEPTION);
  }
}
