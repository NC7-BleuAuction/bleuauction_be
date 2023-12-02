package bleuauction.bleuauction_be.server.menu.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MenuStatus {
  Y("일반회원"),
  N("탈퇴회원");

  private final String value;
}
