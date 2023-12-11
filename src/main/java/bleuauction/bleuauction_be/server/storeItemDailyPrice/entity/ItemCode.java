package bleuauction.bleuauction_be.server.storeItemDailyPrice.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public enum ItemCode {
  S("생선/횟감"),
  F("생선/비횟감"),
  C("갑각류"),
  M("패류"),
  E("기타");

  private final String value;
}

