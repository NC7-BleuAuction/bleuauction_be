package bleuauction.bleuauction_be.server.attach.exception;

import static bleuauction.bleuauction_be.server.attach.type.FileExceptionCode.ATTACH_NOT_FOUND_FILEID;

import bleuauction.bleuauction_be.server.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AttachNotFoundFileIdException extends BaseException {
    private static final String message =
            "[AttachNotFoundException] No Such Attach File, FileNo >>> ";

    public AttachNotFoundFileIdException(Long fileNo) {
        super(ATTACH_NOT_FOUND_FILEID);
        log.warn(message + "{}", fileNo);
    }
}
