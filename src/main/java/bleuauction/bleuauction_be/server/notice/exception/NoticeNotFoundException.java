package bleuauction.bleuauction_be.server.notice.exception;


import bleuauction.bleuauction_be.server.common.exception.BaseException;
import bleuauction.bleuauction_be.server.common.exception.GlobalException;

public class NoticeNotFoundException extends BaseException {
    public static final NoticeNotFoundException EXCEPTION = new NoticeNotFoundException();

    private NoticeNotFoundException() {
        super(GlobalException.NOTICE_NOT_FOUND_EXCEPTION);
    }
}
