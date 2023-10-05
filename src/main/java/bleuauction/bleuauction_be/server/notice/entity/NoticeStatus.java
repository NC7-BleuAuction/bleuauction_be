package bleuauction.bleuauction_be.server.notice.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NoticeStatus {
  Y("사용"), N("삭제");

  private final String value;
}
