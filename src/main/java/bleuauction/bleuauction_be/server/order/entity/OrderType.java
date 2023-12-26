package bleuauction.bleuauction_be.server.order.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderType {
    Q("퀵배송"),
    T("포장");

    private final String value;
}
