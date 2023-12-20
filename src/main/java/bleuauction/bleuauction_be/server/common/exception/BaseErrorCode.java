package bleuauction.bleuauction_be.server.common.exception;

import bleuauction.bleuauction_be.server.common.dto.ErrorDetail;

public interface BaseErrorCode {
  ErrorDetail getErrorDetail();
}
