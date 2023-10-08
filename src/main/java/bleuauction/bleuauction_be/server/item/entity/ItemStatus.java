package bleuauction.bleuauction_be.server.item.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemStatus {
  Y("사용"), N("삭제");

  private final String value;
}
