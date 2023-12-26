package bleuauction.bleuauction_be.server.attach.exception;

import static bleuauction.bleuauction_be.server.attach.type.FileExceptionCode.ATTACH_NOT_FOUND_FILEID;

import bleuauction.bleuauction_be.server.common.exception.BaseException;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AttachUploadBadRequestException extends BaseException {

    private static final String message =
            "[AttachUploadBadRequestException] BadRequest File Upload";

    public AttachUploadBadRequestException() {
        super(ATTACH_NOT_FOUND_FILEID);
        log.warn(message + "{}", LocalDateTime.now());
    }
}
