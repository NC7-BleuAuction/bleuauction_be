package bleuauction.bleuauction_be.server.pay.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PayStatus {
    Y("결제완료"),
    N("결제취소");

    private final String value;
}
