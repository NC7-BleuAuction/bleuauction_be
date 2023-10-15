package bleuauction.bleuauction_be.server.orderMenu.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderMenuStatus {
  Y("주문"),
  N("주문취소");

  private final String value;
}

