package bleuauction.bleuauction_be.server.exception;

import bleuauction.bleuauction_be.server.common.dto.ErrorDetail;

public interface BaseErrorCode {
  ErrorDetail getErrorDetail();
}
