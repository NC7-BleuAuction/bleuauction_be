package bleuauction.bleuauction_be.server.attach.type;


import bleuauction.bleuauction_be.server.common.dto.ErrorDetail;
import bleuauction.bleuauction_be.server.common.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FileExceptionCode implements BaseErrorCode {
    ATTACH_NOT_FOUND_FILEID(HttpStatus.NOT_FOUND.value(), "404-01", "해당 파일을 찾을 수 없습니다."),
    ATTACH_UPLOAD_BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "400-01", "파일 업로드 요청이 잘못되었습니다.");

    private final Integer statusCode;
    private final String errorCode;
    private final String reason;

    @Override
    public ErrorDetail getErrorDetail() {
        return ErrorDetail.of(statusCode, errorCode, reason);
    }
}
