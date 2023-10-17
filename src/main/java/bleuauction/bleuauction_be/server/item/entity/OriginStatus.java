package bleuauction.bleuauction_be.server.item.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OriginStatus {
  D("국내"), I("국외");

  private final String value;
}
