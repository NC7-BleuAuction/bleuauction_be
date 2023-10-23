package bleuauction.bleuauction_be.server.storeItemDailyPrice.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WildFarmStatus {
  W("자연산"),
  F("양식");

  private final String value;
}
