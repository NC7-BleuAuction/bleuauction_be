package bleuauction.bleuauction_be.server.common.exception;

public class InvalidTokenException extends BaseException {

    public static final InvalidTokenException EXCEPTION = new InvalidTokenException();

    public InvalidTokenException() {
        super(GlobalException.INVALID_TOKEN_EXCEPTION);
    }
}
