package bleuauction.bleuauction_be.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberCategory {
  M("일반회원"),
  S("판매자회원"),
  A("관리자");

  private final String value;
}
