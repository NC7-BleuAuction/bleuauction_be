package bleuauction.bleuauction_be.server.store.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UnsupportedType {
  Q("퀵 배송"),
  T("포장");

  private final String value;
}