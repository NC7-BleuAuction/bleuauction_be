package bleuauction.bleuauction_be.server.storeItemDailyPrice.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OriginPlaceStatus {
  ES("동해"),
  WS("서해"),
  SS("남해"),
  JJ("제주"),
  WD("완도"),
  JP("일본"),
  CN("중국"),
  RU("러시아"),
  NW("노르웨이");

  private final String value;
}
