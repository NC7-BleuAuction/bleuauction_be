package bleuauction.bleuauction_be.server.menu.exception;


import bleuauction.bleuauction_be.server.common.exception.BaseException;
import bleuauction.bleuauction_be.server.common.exception.GlobalException;

public class MenuNotFoundException extends BaseException {
    public static final MenuNotFoundException EXCEPTION = new MenuNotFoundException();

    public MenuNotFoundException() {
        super(GlobalException.MENU_NOT_FOUND_EXCEPTION);
    }
}
