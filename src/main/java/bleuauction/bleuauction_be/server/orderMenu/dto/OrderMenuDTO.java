package bleuauction.bleuauction_be.server.orderMenu.dto;

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
  private Long menuNo; // 메뉴 번호
  private Long orderNo; // 주문 번호
  private Long memberNo; // 멤버 번호
}
