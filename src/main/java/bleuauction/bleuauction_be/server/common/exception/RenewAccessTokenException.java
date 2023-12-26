package bleuauction.bleuauction_be.server.common.exception;

import static bleuauction.bleuauction_be.server.common.exception.GlobalException.RENEW_ACCESS_TOKEN_EXCEPTION;

public class RenewAccessTokenException extends BaseException {

    public static final RenewAccessTokenException EXCEPTION = new RenewAccessTokenException();

    public RenewAccessTokenException() {
        super(RENEW_ACCESS_TOKEN_EXCEPTION);
    }
}
