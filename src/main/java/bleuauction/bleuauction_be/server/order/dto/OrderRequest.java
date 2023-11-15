package bleuauction.bleuauction_be.server.order.dto;

import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.orderMenu.dto.OrderMenuDTO;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenu;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Data
public class OrderRequest {
  private Order order;
  private List<OrderMenuDTO> orderMenus;
}