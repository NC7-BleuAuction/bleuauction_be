package bleuauction.bleuauction_be.server.storeItemDailyPrice.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DailyPriceStatus {
  Y("사용"),
  N("삭제");

  private final String value;
}
