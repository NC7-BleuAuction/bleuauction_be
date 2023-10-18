package bleuauction.bleuauction_be.server.pay.dto;

import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.order.entity.OrderStatus;
import bleuauction.bleuauction_be.server.pay.entity.Pay;
import bleuauction.bleuauction_be.server.pay.entity.PayStatus;
import bleuauction.bleuauction_be.server.pay.entity.PayType;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PayInsertRequest {

    private PayType payType = PayType.C;
    private OrderStatus orderStatus = OrderStatus.Y;

//    private Long payNo;

    private Long orderNo;

    @NotNull(message = "결제금액을 입력해주세요.")
    private Integer payPrice;

    // TODO : payStatus 확인하기
    @NotNull
    private PayStatus payStatus;

    private Timestamp payDatetime;

    private Timestamp payCancelDatetime;

    public Pay getPayEntity(Order inserOrderEntity) {
        return Pay.builder()
                .orderNo(inserOrderEntity)
                .payType(PayType.C)
                .payPrice(this.payPrice)
                .payStatus(this.payStatus)
                .payDatetime(this.payDatetime)
                .payCancelDatetime(this.payCancelDatetime)
                .build();
    }
}
