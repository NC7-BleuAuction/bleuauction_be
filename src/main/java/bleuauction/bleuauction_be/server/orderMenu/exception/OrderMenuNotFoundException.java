package bleuauction.bleuauction_be.server.orderMenu.exception;


import bleuauction.bleuauction_be.server.common.exception.BaseException;
import bleuauction.bleuauction_be.server.common.exception.GlobalException;

public class OrderMenuNotFoundException extends BaseException {
    public static final OrderMenuNotFoundException EXCEPTION = new OrderMenuNotFoundException();

    private OrderMenuNotFoundException() {
        super(GlobalException.ORDER_MENU_NOT_FOUND_EXCEPTION);
    }
}
