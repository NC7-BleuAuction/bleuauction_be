package bleuauction.bleuauction_be.server.item.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OriginStatus {
  I("국내"), O("국외");

  private final String value;
}
