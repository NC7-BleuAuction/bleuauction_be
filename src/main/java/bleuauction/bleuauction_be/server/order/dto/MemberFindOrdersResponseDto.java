package bleuauction.bleuauction_be.server.order.dto;

import bleuauction.bleuauction_be.server.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor (access = PRIVATE)
public class MemberFindOrdersResponseDto {
    private List<Order> orders;
    private Long totalPrice;

    @Builder
    public MemberFindOrdersResponseDto(List<Order> orders) {
        this.orders = orders;
        this.totalPrice = orders.stream().mapToLong(Order::calculOrderPrice).sum();
    }
}
