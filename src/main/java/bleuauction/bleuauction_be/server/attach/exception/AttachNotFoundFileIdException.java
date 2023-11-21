package bleuauction.bleuauction_be.server.attach.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AttachNotFoundFileIdException extends RuntimeException{
    private static final String message = "[AttachNotFoundException] No Such Attach File, FileNo >>> ";

    public AttachNotFoundFileIdException(Long fileNo) {
        super(String.format(message + "%d", fileNo));
        log.warn(message + "{}", fileNo);
    }
}
