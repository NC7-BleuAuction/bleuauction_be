package bleuauction.bleuauction_be.server.store.exception;

public class StoreNotFoundException extends RuntimeException {
    private static final String MESSAGE = "[StoreNotFoundException] Not Found Store";

    public StoreNotFoundException() {
        super();
    }

    public StoreNotFoundException(String message) {
        super(message);
    }

    public StoreNotFoundException(Long storeNo) {
        super(String.format(MESSAGE + ", Request Store No >>> %d", storeNo));
    }
}
