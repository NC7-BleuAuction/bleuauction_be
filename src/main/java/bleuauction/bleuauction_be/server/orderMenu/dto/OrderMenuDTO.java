package bleuauction.bleuauction_be.server.orderMenu.dto;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenuStatus;
import lombok.*;

import java.sql.Timestamp;

@Data
public class OrderMenuDTO {
  private Long orderMenuNo;
  private int orderMenuCount;
  private Timestamp regDatetime;
  private Timestamp mdfDatetime;
  private OrderMenuStatus orderMenuStatus;
  private Menu menuNo; // 메뉴 번호
  private Order order; // 주문 번호
  private Member memberNo; // 멤버 번호
}
