package bleuauction.bleuauction_be.server.pay.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PayHistoryNotFoundException extends RuntimeException{
    private static final String MESSAGE = "[PayHistoryNotFoundException] 결제 내역이 존재하지 않습니다.";

    public PayHistoryNotFoundException() {
        super(MESSAGE);
    }

    public PayHistoryNotFoundException(Long payNo) {
        super(String.format(MESSAGE + ", request PayNo %d", payNo));
        log.error(MESSAGE + ", request PayNo {}", payNo);
    }
}
