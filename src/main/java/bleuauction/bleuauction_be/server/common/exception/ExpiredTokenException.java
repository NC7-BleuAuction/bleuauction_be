package bleuauction.bleuauction_be.server.common.exception;

public class ExpiredTokenException extends BaseException {

    public static final ExpiredTokenException EXCEPTION = new ExpiredTokenException();

    public ExpiredTokenException() {
        super(GlobalException.EXPIRED_TOKEN_EXCEPTION);
    }
}
