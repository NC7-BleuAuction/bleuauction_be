package bleuauction.bleuauction_be.server.attach.exception;

import bleuauction.bleuauction_be.server.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

import static bleuauction.bleuauction_be.server.attach.type.FileExceptionCode.ATTACH_NOT_FOUND_FILEID;

@Slf4j
public class AttachUploadBadRequestException extends BaseException {

    private static final String message = "[AttachUploadBadRequestException] BadRequest File Upload";


    public AttachUploadBadRequestException() {
        super(ATTACH_NOT_FOUND_FILEID);
        log.warn(message + "{}", LocalDateTime.now());
    }
}
