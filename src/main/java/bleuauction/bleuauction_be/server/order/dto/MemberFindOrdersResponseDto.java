package bleuauction.bleuauction_be.server.order.dto;

import static lombok.AccessLevel.PRIVATE;

import bleuauction.bleuauction_be.server.order.entity.Order;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class MemberFindOrdersResponseDto {
    private List<Order> orders;
    private Long totalPrice;

    @Builder
    public MemberFindOrdersResponseDto(List<Order> orders) {
        this.orders = orders;
        this.totalPrice = orders.stream().mapToLong(Order::calculOrderPrice).sum();
    }
}
