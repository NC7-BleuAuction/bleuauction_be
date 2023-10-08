package bleuauction.bleuauction_be.server.menu.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MenuSize {
  S("소"),
  M("중"),
  L("대");

  private final String value;
}
