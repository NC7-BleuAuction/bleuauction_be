package bleuauction.bleuauction_be.server.order.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderNotFoundException extends RuntimeException{

    private static final String DEFAULT_CONSTRUCTOR_MESSAGE = "[OrderNotFoundException] Not Found Order";
    private static final String MESSAGE = DEFAULT_CONSTRUCTOR_MESSAGE + ", requestOrderNo >>> ";

    public OrderNotFoundException() {
        super(DEFAULT_CONSTRUCTOR_MESSAGE);
    }

    public OrderNotFoundException(String message) {
        super(DEFAULT_CONSTRUCTOR_MESSAGE + " " + message);
    }

    public OrderNotFoundException(Long orderNo) {
        super(String.format(MESSAGE + "%d", orderNo));
        log.warn(MESSAGE + "{}", orderNo);
    }
}
