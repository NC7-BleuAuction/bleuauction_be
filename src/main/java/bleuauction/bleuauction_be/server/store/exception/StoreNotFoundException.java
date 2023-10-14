package bleuauction.bleuauction_be.server.store.exception;

public class StoreNotFoundException extends RuntimeException {
    public StoreNotFoundException() {
        super();
    }
    public StoreNotFoundException(String message) {
        super(message);
    }
    public StoreNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public StoreNotFoundException(Throwable cause) {
        super(cause);
    }
    protected StoreNotFoundException(String message, Throwable cause, boolean enableSuppresstion, boolean writableStackTrace) {
        super(message, cause, enableSuppresstion, writableStackTrace);
    }

}
