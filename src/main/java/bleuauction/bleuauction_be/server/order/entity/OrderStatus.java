package bleuauction.bleuauction_be.server.order.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    Y("주문"),
    N("주문취소");

    private final String value;
}
