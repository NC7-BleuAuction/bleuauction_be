package bleuauction.bleuauction_be.server.common.exception;


import bleuauction.bleuauction_be.server.common.dto.ErrorDetail;
import bleuauction.bleuauction_be.server.common.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> internalServerExceptionHandler(Exception e) {
        log.error("ExceptionHandler - Exception : {}", String.valueOf(e));
        e.printStackTrace();
        GlobalException internalServerError = GlobalException.INTERNAL_SERVER_ERRORS;
        ErrorResponse errorResponse = new ErrorResponse(internalServerError.getErrorDetail());

        return ResponseEntity.status(HttpStatus.valueOf(internalServerError.getStatusCode()))
                .body(errorResponse);
    }

    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<ErrorResponse> authExceptionHandler(BaseException e) {
        log.error("ExceptionHandler - BaseException: {}", String.valueOf(e));
        BaseErrorCode code = e.getErrorCode();
        ErrorDetail errorDetail = code.getErrorDetail();
        ErrorResponse errorResponse = new ErrorResponse(errorDetail);
        return ResponseEntity.status(HttpStatus.valueOf(errorDetail.getStatusCode()))
                .body(errorResponse);
    }
}
