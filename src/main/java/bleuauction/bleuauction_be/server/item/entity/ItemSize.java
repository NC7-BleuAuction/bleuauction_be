package bleuauction.bleuauction_be.server.item.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemSize {
  S("소"), M("중"), L("대");

  private final String value;
}
