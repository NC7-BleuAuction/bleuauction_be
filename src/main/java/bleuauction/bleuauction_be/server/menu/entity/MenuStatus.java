package bleuauction.bleuauction_be.server.menu.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MenuStatus {
  Y("사용"),
  N("삭제");

  private final String value;
}
