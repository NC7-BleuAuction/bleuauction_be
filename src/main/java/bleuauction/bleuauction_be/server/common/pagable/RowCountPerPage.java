package bleuauction.bleuauction_be.server.common.pagable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RowCountPerPage {

  REVIEW(4),
  ANSWER(2);

  private final int value;
}
