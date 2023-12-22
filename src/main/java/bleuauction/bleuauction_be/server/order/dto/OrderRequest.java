package bleuauction.bleuauction_be.server.order.dto;


import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.orderMenu.dto.OrderMenuDTO;
import java.util.List;
import lombok.Data;

@Data
public class OrderRequest {
    private Order order;
    private List<OrderMenuDTO> orderMenus;
}
