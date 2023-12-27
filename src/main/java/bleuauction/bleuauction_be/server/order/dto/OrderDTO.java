package bleuauction.bleuauction_be.server.order.dto;


import bleuauction.bleuauction_be.server.member.entity.Address;
import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.order.entity.OrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private OrderType orderType;
    private int orderPrice;
    private String orderRequest;
    private String recipientPhone;
    private String recipientName;
    private String recipientZipcode;
    private String recipientAddr;
    private String recipientDetailAddr;



    public Address buildRecipientAddress() {
        return new Address(recipientZipcode, recipientAddr, recipientDetailAddr);
    }
    public static Order toEntity(OrderDTO orderDTO) {
        return Order.builder()
                .orderType(orderDTO.getOrderType())
                .orderPrice(orderDTO.getOrderPrice())
                .orderRequest(orderDTO.getOrderRequest())
                .recipientPhone(orderDTO.getRecipientPhone())
                .recipientName(orderDTO.getRecipientName())
                .recipientZipcode(orderDTO.getRecipientZipcode())
                .recipientAddr(orderDTO.getRecipientAddr())
                .recipientDetailAddr(orderDTO.getRecipientDetailAddr())
                .build();
    }
}
