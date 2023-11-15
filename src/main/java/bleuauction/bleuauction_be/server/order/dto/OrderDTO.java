package bleuauction.bleuauction_be.server.order.dto;

import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.order.entity.OrderStatus;
import bleuauction.bleuauction_be.server.order.entity.OrderType;
import bleuauction.bleuauction_be.server.orderMenu.dto.OrderMenuDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderDTO {
  private OrderType orderType;
  private int orderPrice;
  private String orderRequest;
  private String recipientPhone;
  private String recipientName;
  private String recipientZipcode;
  private String recipientAddr;
  private String recipientDetailAddr;

  public static Order toEntity(OrderDTO orderDTO) {
    Order order = new Order();
    order.setOrderType(orderDTO.getOrderType());
    order.setOrderPrice(orderDTO.getOrderPrice());
    order.setOrderRequest(orderDTO.getOrderRequest());
    order.setRecipientPhone(orderDTO.getRecipientPhone());
    order.setRecipientName(orderDTO.getRecipientName());
    order.setRecipientZipcode(orderDTO.getRecipientZipcode());
    order.setRecipientAddr(orderDTO.getRecipientAddr());
    order.setRecipientDetailAddr(orderDTO.getRecipientDetailAddr());
    return order;
  }
}